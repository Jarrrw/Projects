// Declares the audio file
const audio = new Audio("above-the-clouds.mp3");

// Titles and their time in the audio
titlesTime = {
    "Beginning": 0,
    "Speed Up": 20,
    "First Quarter": 90,
    "Middle": 180,
    "Third Quarter": 270,
    "End": 313
};

// Set the current title to the beginning
currTitle = "Beginning";

// Loads the list of titles as buttons on the page
function loadTitleList() {
    // Gets the div that will hold the list of titles
    list = document.getElementById('titlelist');
    // Clears the list
    list.innerHTML = "";
    // For each title in the titlesTime object
    for(var key in titlesTime) {
        // Create a button with the title as the text
        button = document.createElement('button');
        button.innerHTML = key;
        // Add the class button-33-2 to the button
        button.classList.add('button-33-2')
        // Add an onclick function to the button
        button.onclick = function() {
            // Set the current title to the title of the button
            document.getElementById('currTitle').innerHTML = "<strong>Current Title: </strong>" + this.innerHTML;
            // Set the audio's current time to the time corresponding to the title in the titlesTime object
            audio.currentTime = titlesTime[this.innerHTML];
            // Set the current title to the title of the button
            currTitle = this.innerHTML;

        };
        // Add the button to the list
        list.appendChild(button);
        // Add a line break to the list
        list.appendChild(document.createElement('br'));
    }
}

// This function to show the form that asked for the new title name
function addTitle() {
    // Get the form
    form = document.getElementById('newtitle');
    // If the form is visible, hide it
    // If the form is hidden, show it
    if (form.style.display === 'block') {
        form.style.display = 'none';
    } else {
        form.style.display = 'block';
    }
}

// This function adds a new title to the titlesTime object
function newTitle() {
    titlesTime[document.getElementById('newtitleinput').value] = audio.currentTime;
    loadTitleList();
    document.getElementById('newtitle').style.display = 'none';
}

// This function is called when the window loads
window.onload = function () {
    // Load the initial list of titles
    loadTitleList();
    // Gets the time element from the page
    time = document.getElementById('time');
    // Calculates the remaining time the track has
    var remainingTime = audio.duration - audio.currentTime;
    // Calculates the minutes and seconds of the remaining time
    var minutes = Math.floor(remainingTime / 60);
    var seconds = Math.floor(remainingTime % 60);
    // Sets the time element to the remaining time
    time.innerHTML = minutes + ":" + (seconds < 10 ? "0" : "") + seconds;

    // Does the same thing as the above every time the time is updated
    audio.ontimeupdate = function() {
        var remainingTime = audio.duration - audio.currentTime;
        var minutes = Math.floor(remainingTime / 60);
        var seconds = Math.floor(remainingTime % 60);
        time.innerHTML = minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    };
}

// This function plays or pauses the audio
function playPause() {
    button = document.getElementById('playpause');
    if (audio.paused) {
        button.innerHTML = 'Pause';
        audio.play();
    } else {
        button.innerHTML = 'Play';
        audio.pause();
    }
}

// This function rewinds the audio by 5 seconds
function rewind() {
    audio.currentTime += -5;
}

// This function fast forwards the audio by 5 seconds
function forward() {
    audio.currentTime += 5;
}

// This function removes the current title from the titlesTime object
function removeTitle() {
    delete titlesTime[currTitle];
    loadTitleList();
    document.getElementById('currTitle').innerHTML = "<strong>Current Title: </strong>None";
}



