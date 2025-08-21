var t;
var urlParams = new URLSearchParams(window.location.search);
var UserID = urlParams.get('id');
let movies = {};

async function getSearch() {
    
    query = document.getElementById("movieSearch").value;
    var urls;
    if (query.length <= 2) {
        console.log("short");
            urls = [
                '/CollectAPP/search/getPartial.php?query=' + query,
            ];
        }
    else {
        console.log("long");
        urls = [
            '/CollectAPP/search/getMovies.php?query=' + query,
        ];
    }

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url).then(res => res.json()))
        );


        // responses is now an array of all the results

        // Example: access individual results
        const favorites = responses[0];
        showMovies(favorites)

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}

// This function displays the results of the Ajax request
function showMovies(favs) {
    // Get the div that will display the movie
    var div = document.getElementById("response");
    // Deletes the previous search results
    div.innerHTML = "";
    // Parses the JSON response

    var response = favs
    
    if (!Array.isArray(response)) {
        response = response.results;
    }
    // Checks to see if there are no search results
    if (!response || response.length == 0) {
        // Outputs a message to the user
        var noResults = document.createElement("p");
        noResults.id = "noResults";
        noResults.innerText = "No Results Found";
        div.appendChild(noResults);
        return;
    }
    response.forEach(mov => {
        // Positions the div that holds the movies
        div.style.position = "relative";
        
        // Creates the div that will hold the movie
        var movie = document.createElement("div");
        movie.id = "movie";
        // Creates the elements that will display the title and image of the movie
        var image = document.createElement("img");
        
        var details = document.createElement("p");

        details.innerHTML = "<h3 id=\"detail\">" + mov.primaryTitle + "</h3>";
        details.innerHTML += "<p id=\"detail\">" + mov.startYear + "</p>";
        details.innerHTML += "<p id=\"detail\">" + mov.contentRating + "</p>";
        details.id = "title";

        // Checks to see if the movie has an image
        if (mov.primaryImage == null) {
            mov.primaryImage = "No Image.png";
        }
        image.src = mov.primaryImage;
    
        // Creates the button that will take the user to a new page that will display more information to the user
        var button = document.createElement("button");
        button.id = "button";
        button.textContent = "View More Info"
        button.addEventListener("click", function() {
            ViewMore(mov);
        })

        var favorite = document.createElement("button");
        favorite.id = "favoriteButton";
        favorite.textContent = "Favorite Item"
        favorite.addEventListener("click", function() {
            FavoriteItem(mov);
        })

        // Adds everything to the main div

        details.appendChild(button);
        details.appendChild(favorite);
        movie.appendChild(image);
        movie.appendChild(details)
        div.appendChild(movie);
    });
}





// Sends the user to the another page with more information about the movie with the corresponding ID
function ViewMore(mov) {
    window.location.href = `movieinfo.html?id=${mov.id}&UserID=${UserID}`;
}


async function FavoriteItem(mov) {
    
    const urls = [
        `/CollectAPP/collection/setFavorite.php?id=${UserID}&movie=${mov.id}`,
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url))
        );

        getFavorites();

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    console.log("Calling getfavorites")
    getFavorites();
    getSearch();
    
    obj = document.getElementById("movieSearch");
    obj.onkeydown = getSearch;
    
    SearchType();
});

