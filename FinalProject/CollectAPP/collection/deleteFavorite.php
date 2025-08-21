<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); 
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
header('Content-Type: application/json');

$host = "localhost:3306";  // Change if using a remote server
$username = "jarrrwco_jarrrw";         // MySQL username
$password = "JarMad0306#";             // MySQL password (default is empty for XAMPP)
$database = "jarrrwco_CollectAPP";


// Create connection
$conn = new mysqli($host, $username, $password,  $database);

$id = isset($_GET['id']) ? $_GET['id'] : '';
$movie = isset($_GET['movie']) ? $_GET['movie'] : '';


$stmt = $conn->prepare("DELETE FROM favorites WHERE id = ? and MovieID = ?");
$stmt->bind_param("is", $id, $movie);

$stmt->execute();
header("Location: http://127.0.0.1:5501/CollectAPP/search/search.html?id=". $id );