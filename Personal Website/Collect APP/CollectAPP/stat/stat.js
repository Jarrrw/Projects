var t;
function SearchType() {
    if (t) window.clearTimeout(t);
    t = window.setTimeout("GetType()",0);
}
function SearchGenre() {
    if (t) window.clearTimeout(t);
    t = window.setTimeout("GetGenres()",200);
}
function SearchCountry() {
    if (t) window.clearTimeout(t);
    t = window.setTimeout("GetCountry()",200);
}
function SearchLanguage() {
    if (t) window.clearTimeout(t);
    t = window.setTimeout("GetLanguages()",200);
}

// This function sets up the Ajax request that will get the number of types of Entertainment

function GetType() {
    // assemble the PHP filename
    var url = '/CollectAPP/stat/getType.php';
    // DisplayResults will handle the Ajax response
    ajaxCallback = function() {
        PrintResult("type");
    }
    // Send the Ajax request
    ajaxRequest(url);
}

// This function sets up the Ajax request that will get the number of genres
function GetGenres() {
    // assemble the PHP filename
    var url = '/CollectAPP/stat/getGenres.php'
    // DisplayResults will handle the Ajax response
    ajaxCallback = function() {
        PrintResult("genre");
    }
    // Send the Ajax request
    ajaxRequest(url);
}
// This function sets up the Ajax request that will get the number of countries
function GetCountry() {
    // assemble the PHP filename
    var url = '/CollectAPP/stat/getCountry.php'
    // DisplayResults will handle the Ajax response
    ajaxCallback = function() {
        PrintResult("country");
    }
    // Send the Ajax request
    ajaxRequest(url);
}
// This function sets up the Ajax request that will get the number of Languages
function GetLanguages() {
    // assemble the PHP filename
    var url = '/CollectAPP/stat/getLanguages.php'
    // DisplayResults will handle the Ajax response
    ajaxCallback = function() {
        PrintResult("language");
    }
    // Send the Ajax request
    ajaxRequest(url);
}

// This function prints out the results and takes a parameter to determine which type to print out
function PrintResult(type) {
    // Gets the element that will display the result
    var element = document.getElementById(type);
    var response = JSON.parse(ajaxreq.responseText)
    
    if (type == "type") {
        element.innerText = "Entertainment Types Listed: " + response.length;
        // Starts the next search in the series
        SearchGenre();
    }
    if (type == "genre") {
        element.innerText = "Genres Listed: " + response.length;
        // Starts the next search in the series
        SearchCountry();
    }
    if (type == "country") {
        element.innerText = "Countries Listed: " + response.length;
        // Starts the next search in the series
        SearchLanguage();
    }
    if (type == "language") {
        element.innerText = "Languages Listed: " + response.length;
        
    }
    

}

// Adds the functionality to the function on this page
function ChangePage(event) {
    var newPage = event.target.id;
    window.location.href = "/CollectAPP/" + newPage + "/" + newPage + ".html";
}