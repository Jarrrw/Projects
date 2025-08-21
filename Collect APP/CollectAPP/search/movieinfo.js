var t;

// Gets the id of the movie from the URL
const urlParams = new URLSearchParams(window.location.search);
const movieID = urlParams.get('id');
const UserID = urlParams.get('UserID');
function startSearch() {
    if (t) window.clearTimeout(t);
    t = window.setTimeout("liveSearch()",0);
}

// This function creates the Ajax request
function liveSearch() {
    // Assembles the URL for the Ajax request
    var url = '/CollectAPP/search/getMovie.php?query=' + movieID;
    // DisplayResults will handle the Ajax response
    ajaxCallback = ShowAllInfo;
    // Send the Ajax request
    ajaxRequest(url);
}

function ShowAllInfo() {
    var mov = JSON.parse(ajaxreq.response)
    // Sets teh title of the page to the movie title
    var title = document.getElementById("movietitle");
    title.innerText = mov.primaryTitle;
    // Gets the div that will hold the movie info
    var infoDiv = document.getElementById("info");

    // Creates the elements that will go in the information div
    var img = document.createElement("img");
    var basicInfo = document.createElement("p")
    var type = document.createElement("p");
    var description = document.createElement("p");
    var categories = document.createElement("p");
    var credit = document.createElement("p");

    // Adds the corresponding information to each element of the div using the object retrieved in the search
    img.src = mov.primaryImage;
    type.id = "type";
    type.innerHTML = mov.type;
    description.innerHTML = "<strong>Description: </strong>" + mov.description
    description.id = "description"
    categories.innerHTML = "<strong>Category: </strong>";
    categories.id = "categories"
    
    mov.interests.forEach((cat, index) => {
        if (index == mov.interests.length - 1) {
            categories.innerHTML += cat
        }
        else {
            categories.innerHTML += cat + ", "
        }
    });
    

    credit.innerHTML = "<strong>Director: </strong>"
    credit.id = "credit";
    mov.directors.forEach((dir, index) => {
        if (index == mov.directors.length - 1) {
            credit.innerHTML += dir.fullName + "<br><br>"
        }
        else {
            credit.innerHTML += dir.fullName + ", "
        }
    });
    credit.innerHTML += "<strong>Cast: </strong>"
    mov.cast.forEach((cast, index) => {
        if (index == mov.cast.length - 1) {
            credit.innerHTML += cast.fullName + "<br><br>"
        }
        else {
            credit.innerHTML += cast.fullName + ", "
        }
    });

    credit.innerHTML += "<strong>Genres: </strong>";
    mov.genres.forEach((gen, index) => {
        if (index == mov.genres.length - 1) {
            credit.innerHTML += gen + "<br><br>"
        }
        else {
            credit.innerHTML += gen + ", "
        }
    });
    basicInfo.id = "basic";
    // Adds all the elements to the main div
    basicInfo.innerHTML = mov.startYear + " * " + mov.contentRating + " * " + mov.runtimeMinutes + "mins";
    infoDiv.appendChild(img);
    infoDiv.appendChild(basicInfo);
    infoDiv.appendChild(description);
    infoDiv.appendChild(categories);
    infoDiv.appendChild(credit);

}

// Starts the search when this page is loaded
document.addEventListener("DOMContentLoaded", function() { 
    startSearch();
 });

 function ChangePage() {
    window.location.href = "search.html?id=" + UserID;
 }