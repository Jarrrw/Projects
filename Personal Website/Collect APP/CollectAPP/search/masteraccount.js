const urlParams = new URLSearchParams(window.location.search);
const UserID = urlParams.get('UserID');

async function getAccounts() {
    
    const urls = [
        '/CollectAPP/search/getAccounts.php'
    ];

    try {
        const responses = await Promise.all(
            urls.map(url => fetch(url).then(res => res.json()))
        );


        // responses is now an array of all the results
        console.log("All results:", responses);

        // Example: access individual results
        const accounts = responses[0];
        showAccounts(accounts)

    } catch (error) {
        console.error("One or more fetches failed:", error);
    }
}

function showAccounts(accounts) {
    var div = document.getElementById("accountlist");

    accounts.forEach(element => {
        var account = document.createElement("div");
        account.id = "detail";
        account.innerHTML += `<p>ID: ${element.id}</p>`
        account.innerHTML += `<p>Name: ${element.name}</p>`
        account.innerHTML += `<p>Email: ${element.email}</p>`
        account.innerHTML += `<p>Account Creation Time: ${element.created_at}</p>`
        account.innerHTML += `<p>Last Login Time: ${element.lastLogin}</p>`
        account.innerHTML += `<p>Last Login Time: ${element.numLogin}</p>`
        account.innerHTML += `<p>Failed Logins: ${element.failedLogin}</p>`
        
        div.appendChild(account);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    getAccounts();
});

function ChangePage() {
    window.location.href = "search.html?id=" + UserID;
}