fetch('/Homepage/navbar.html')
    .then(response => response.text())
    .then(data => {
      document.getElementById('navbarheader').innerHTML = data;
    })
    .catch(error => console.error('Error loading navbar:', error));