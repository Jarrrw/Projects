const urlParams = new URLSearchParams(window.location.search);
const UserID = urlParams.get('UserID');
async function getMovie(mov) {
    
    const urls = [
        '/CollectAPP/search/getMovie.php?query=' + mov.MovieID,
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url).then(res => res.json()))
        );


        // responses is now an array of all the results
        console.log("All results:", responses);

        // Example: access individual results
        const favorites = responses[0];
        console.log(mov.addTimestamp)
        showMovies(favorites, mov.id, mov.addTimestamp)

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}
async function getFavorites() {
    console.log("Get Favorites")
    const urls = [
        'MasterCollection.php',
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url).then(res => res.json()))
        );

        // responses is now an array of all the results
        console.log("All results:", responses[0]);

        // Example: access individual results
        const favorites = responses[0];
        getMovies(favorites)

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}

async function getMovies(favs) {
    var div = document.getElementById("collectionlist");
    const response = favs
    
    console.log(response[0])
    // Checks to see if there are no search results
    if (!response || response.length == 0) {
        // Outputs a message to the user
        var noResults = document.createElement("p");
        noResults.id = "noResults";
        noResults.innerText = "No Results Found";
        div.appendChild(noResults);
        return;
    }
    
    div.innerHTML = "";
    console.log("Movies to process:", response.length);
    response.forEach(element => {
        console.log(element.addTimestamp);
        getMovie(element);
    });
}

async function showMovies(favs, AccountID, time) {
    // Get the div that will display the movie
    var div = document.getElementById("collectionlist");
    // Deletes the previous search results
    // Parses the JSON response
    var mov = favs
    console.log(mov);
    
        var ownerName = await getName(AccountID);
        // Positions the div that holds the movies
        div.style.position = "relative";
        
        // Creates the div that will hold the movie
        var movie = document.createElement("div");
        movie.id = "movies";
        // Creates the elements that will display the title and image of the movie
        var image = document.createElement("img");
        
        var details = document.createElement("p");

        
        details.innerHTML = "<h3 id=\"detail\">" + mov.primaryTitle + "</h3>";
        details.innerHTML += "<p id=\"detail\">" + mov.startYear + "</p>";
        details.innerHTML += "<p id=\"detail\">" + mov.contentRating + "</p>";
        details.innerHTML += "<p id=\"detail\"> ADDED: " + time + "</p>";
        details.innerHTML += "<p id=\"detail\"> Owner: " + ownerName + "</p>";
        
        details.id = "title";

        

        // Checks to see if the movie has an image
        if (mov.primaryImage == null) {
            mov.primaryImage = "No Image.png";
        }
        image.src = mov.primaryImage;
    
        

        // Adds everything to the main div

        
        movie.appendChild(image);
        movie.appendChild(details)
        
        div.appendChild(movie);
}

document.addEventListener('DOMContentLoaded', function() {
    getFavorites();
});

function ChangePage() {
    window.location.href = "search.html?id=" + UserID;
}

async function getName(AccountID) {
    const urls = [
        '/CollectAPP/search/getAccount.php?id=' + AccountID,
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url).then(res => res.json()))
        );


        // responses is now an array of all the results
        console.log("All results:", responses);

        // Example: access individual results
        const account = responses[0];
        console.log(account[0].name)
        return account[0].name

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}