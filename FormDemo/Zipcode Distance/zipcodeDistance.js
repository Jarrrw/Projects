

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('submit').addEventListener("click", function (event) {
        event.preventDefault(); // Prevent form reload (if applicable)
        
        var zip1 = document.getElementById("zip1").value
        var zip2 = document.getElementById("zip2").value
        fetchZipData(zip1, zip2)
            .then(data => {
                var distance = calcDistance(data, zip1, zip2);
                printResults(data, zip1, zip2, distance)
            })
            .catch(error => {
                console.error("Error fetching ZIP data:", error);
            });
        })   
});


function fetchZipData(zip1, zip2) {
    if (!zip1 && !zip2) {
        alert("Please enter a ZIP code.");
        return Promise.reject("No ZIP code provided");
    }

    return fetch(`getData.php?zip1=` + zip1 + "&zip2=" + zip2)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        });
}

function calcDistance(data, zip1, zip2) {
    const zip1Data = data[zip1];
    const zip2Data = data[zip2];

    var lat1 = zip1Data.latitude
    var lat2 = zip2Data.latitude

    var lon1 = zip1Data.longitude
    var lon2 = zip2Data.longitude

    // distance between latitudes
    // and longitudes
    let dLat = (lat2 - lat1) * Math.PI / 180.0;
    let dLon = (lon2 - lon1) * Math.PI / 180.0;
           
    // convert to radiansa
    lat1 = (lat1) * Math.PI / 180.0;
    lat2 = (lat2) * Math.PI / 180.0;
         
    // apply formulae
    let a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
    let rad = 6371;
    let c = 2 * Math.asin(Math.sqrt(a));
    console.log(rad * c)
    return rad * c;
}

function printResults(data, zip1, zip2, distance) {
    const zip1Data = data[zip1];
    const zip2Data = data[zip2];

    var lat1 = zip1Data.latitude
    var lat2 = zip2Data.latitude

    var lon1 = zip1Data.longitude
    var lon2 = zip2Data.longitude

    const resultText = `
        <p>ZIP1 (${zip1}):</p>
        <p>Latitude: ${zip1Data.latitude}, Longitude: ${zip1Data.longitude}</p>
        <p>ZIP2 (${zip2}):</p>
        <p>Latitude: ${zip2Data.latitude}, Longitude: ${zip2Data.longitude}</p>
        <p>Distance Between Them: ${distance.toFixed(2)} KM
                    `;
    div = document.getElementById("answer");
    div.innerHTML = resultText
}