
document.addEventListener('DOMContentLoaded', loadNavbar);

async function loadNavbar() {
  const response = await fetch('/Homepage/navbar.html'); // Adjust path if needed
  const navbarHTML = await response.text();
  document.getElementById('navbarheader').innerHTML = navbarHTML;
}