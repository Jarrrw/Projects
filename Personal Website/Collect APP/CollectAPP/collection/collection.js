var t;
var urlParams = new URLSearchParams(window.location.search);
var UserID = urlParams.get('id');
let movieID;
let addTime;


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
        showMoviesC(favorites, mov.addTimestamp)

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}
async function getFavorites() {
    console.log("Get Favorites")
    const urls = [
        '/CollectAPP/collection/getFavorites.php?id=' + UserID,
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
    var div = document.getElementById("collection");
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
    var div = document.getElementById("collection");
    div.innerHTML = "";
    console.log("Movies to process:", response.length);
    response.forEach(element => {
        console.log(element.addTimestamp);
        getMovie(element);
    });
}

function showMoviesC(favs, time) {
    // Get the div that will display the movie
    var div = document.getElementById("collection");
    // Deletes the previous search results
    // Parses the JSON response
    var mov = favs
    console.log(mov);
    
    
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
        details.id = "title";

        

        // Checks to see if the movie has an image
        if (mov.primaryImage == null) {
            mov.primaryImage = "No Image.png";
        }
        image.src = mov.primaryImage;
    
        // Creates the button that will take the user to a new page that will display more information to the user
        var button = document.createElement("button");
        button.id = "viewMore";
        button.textContent = "View More Info"
        button.addEventListener("click", function() {
            ViewMore(mov);
        })

        var favorite = document.createElement("button");
        favorite.id = "favorite";
        favorite.textContent = "Remove From Favorite"
        favorite.addEventListener("click", function() {
            movie.innerHTML="";
            movie.remove();
            RemoveFavorite(mov);
            
        })

        // Adds everything to the main div

        
        movie.appendChild(image);
        movie.appendChild(details)
        movie.appendChild(button);
        movie.appendChild(favorite);
        div.appendChild(movie);
}



async function RemoveFavorite(mov) {
    
    const urls = [
        `/CollectAPP/collection/deleteFavorite.php?id=${UserID}&movie=${mov.id}`,
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url))
        );

        // responses is now an array of all the results
        console.log("All results:", responses);

        // Example: access individual results
        const favorites = responses[0];

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}