const urlParams = new URLSearchParams(window.location.search);
const userID = urlParams.get('id');
var months;
var eventsMonth = {};

var overallDate;

// Gets events for a particular user
async function getEvents(month, year) {
  
  month = parseInt(month) + 1
  console.log(month)
  const urls = [
      `GetEvents.php?userID=${parseInt(userID)}&month=${month}&year=${year}`
  ];

  try {
      const responses = await Promise.all(
          urls.map(url => fetch(url).then(res => res.json()))
      );


      // responses is now an array of all the results
      
      console.log("All results:", responses);

      // Example: access individual results
      const events = responses[0];
      
      SetEvents(events)

  } catch (error) {
      console.error("One or more fetches failed:", error);
  }
}

function SetEvents(events) {
  eventsMonth = {};
  events.forEach(element => {
    day = element.day;
    if (!eventsMonth[day]) {
      eventsMonth[day] = [];
    }
    eventsMonth[day].push(element)
  });
  console.log(eventsMonth)
}
 
 
 function buildDateForm() {
    months = ["January", "February", "March", "April", "May",  "June", "July", "August", "September", " October", "November", "December"];

    $('#datePicker').append('<select id="month"></select>');
  
    for(var i = 0; i < months.length;i++) {
       $('#month').append('<option value="'+i+'">'+months[i]+'</option')
    }

    $('#datePicker').append('<select id="year"></select>');

    for(i = 1990; i < 2100; i++) {
       $('#year').append('<option value="'+i+'">'+i+'</option>')
    }

    $('#datePicker').append('<button id="submit">Go!</button>');

    // set date to current month and year
    var d = new Date();
    var n = d.getMonth();
    var y = d.getFullYear();
    $('#month option:eq('+n+')').prop('selected', true);
    $('#year option[value="'+y+'"]').prop('selected', true);
  }

  async function calendar(date) {
     $("#myCal").empty();

    if (date == null) {
       date = new Date;
     }

     day = date.getDate();
     month = date.getMonth();
     year = date.getFullYear();

     overallDate = date;
     
     await getEvents(month, year);
     months = new Array('January','February','March','April','May','June', 'July','August','September','October','November','December');
     this_month = new Date(year, month, 1);
     next_month = new Date(year, month + 1, 1);
   
     days = new Array('Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat');
     first_week_day = this_month.getDay(); // day of the week of the first day
     days_in_this_month = Math.round((next_month.getTime() - this_month.getTime())  / (1000 * 60 * 60 * 24));

     $('#myCal').append('<table id="myCalendar"></table>');
     $('#myCalendar').append('<thead><tr></tr></thead>');
  
     for (var i=0; i < days.length; i++) {
       $('#myCalendar thead tr').append('<th>'+days[i]+'</th>')
     }
  
     $('#myCalendar').append('<tbody></tbody>');
     $('tbody').append('<tr>');
   
     for(week_day = 0; week_day < first_week_day; week_day++)  {
       $('tbody tr').append('<td id="'+week_day+'"></td>');
     }
  
     week_day = first_week_day;

     for (day_counter=1; day_counter <= days_in_this_month; day_counter++) {
       week_day %= 7;
     
       if (week_day == 0) {
         // go to the next line of the calendar
         $('tbody').append('</tr><tr>');
       }
       
       let s = '';
        const cellID = `day${day_counter}`;
        s += `<td id="${cellID}">${day_counter}`;
        let dayEvents;
        if (eventsMonth[day_counter] != null) {
          dayEvents = eventsMonth[day_counter];
        }
        else if (eventsMonth["0" + day_counter] != null) {
          dayEvents = eventsMonth["0" + day_counter];
        }
        if (dayEvents != null) {
          
          dayEvents.forEach((element, index) => {
            const btnID = `view-${day_counter}-${index}`;
            s += `<br><button id="${btnID}">${element.title}</button>`;
          });
        }
        s += '</td>';
        $('tbody tr:last').append(s);
        document.getElementById(cellID).addEventListener("click", function() {
          console.log("Hello")
          document.getElementById("event").className = "";
          document.getElementById("viewMore").className = "hidden";

          console.log(year);
          console.log(month);
          const day = parseInt(cellID.match(/\d+/)[0]);
          console.log(day)
          const paddedMonth = String(month + 1).padStart(2, '0'); // "04"
          const paddedDay = String(day).padStart(2, '0');
          document.getElementById("dateInput").value = year + "-" + paddedMonth + "-" + paddedDay;
        })
        // 🔁 After the HTML is added, bind the event listeners
        if (dayEvents != null) {
          dayEvents.forEach((element, index) => {
          const btnID = `view-${day_counter}-${index}`;
          document.getElementById(btnID).addEventListener("click", function (event) {
            event.stopPropagation();
            document.getElementById("event").className = "hidden";
            ViewMore(element);
          });
      });
    }
       week_day++;
    }
  }

 // Get the form
$().ready(function(){
  const form = document.getElementById('form');
  form.addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission
  

  
    // You can now send the date to the server via AJAX or handle the form as needed
    const formData = new FormData(form);
  
    fetch('CreateEvent.php', {
      method: 'POST',
      body: formData
    })
    .then() // Handle the response
    .then(function (){
      
      calendar(overallDate)
    })
    .catch((error) => {
      console.error('Error:', error);
    });
  });
});

function ViewMore(event) {
  console.log("hey")
  var div = document.getElementById("viewMore")
  div.className = "";
  var eventDetails = document.createElement("div")
  head = document.createElement("h1");
  head.innerText = event.title;
  head.id = "eventTitle"
  eventDetails.id = "details"
  var deleteButton = document.createElement("button");
  deleteButton.innerText = "Delete Event"
  deleteButton.id = "deleteButton"
  deleteButton.addEventListener("click", function() {
    DeleteEvent(event.eventID);
    div.className = "hidden";
  })
  div.innerHTML = "";
  eventDetails.innerHTML += '<p>' + months[event.month - 1] + " " + event.day + ", " + event.year + '</p>'
  if (event.endTime != null && event.startTime != null) {
    eventDetails.innerHTML += '<p>From ' + convertTo12Hour(event.startTime) + ' to ' + convertTo12Hour(event.endTime); + '</p>';
  }
  else if (event.endTime == null && event.startTime != null) {
    eventDetails.innerHTML += '<p>' + convertTo12Hour(event.startTime) + '</p>';
  }
  else if (event.endTime != null && event.startTime == null) {
    eventDetails.innerHTML += '<p>' + convertTo12Hour(event.endTime) + '</p>';
  }
  if (event.description != null) {
    eventDetails.innerHTML += '<p>' + event.description + '</p>'
  }
  eventDetails.appendChild(deleteButton);
  div.appendChild(head);
  div.appendChild(eventDetails);
  
}

function convertTo12Hour(timeStr) {
  const [hours, minutes] = timeStr.split(":");
  const date = new Date();
  date.setHours(+hours);
  date.setMinutes(+minutes);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

async function DeleteEvent(id) {
  console.log(id);
  const urls = [
      `DeleteEvent.php?id=${id}`
  ];
  const response = await fetch(urls, {
    method: "GET",
    credentials: "include", // needed if your PHP uses cookies/sessions
  });
  try {
      // Example: access individual results
      calendar(overallDate);
      

  } catch (error) {
      console.error("One or more fetches failed:", error);
  }
}