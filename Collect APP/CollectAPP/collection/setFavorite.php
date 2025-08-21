<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); 
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

$host = "localhost:3306";  // Change if using a remote server
$username = "jarrrwco_jarrrw";         // MySQL username
$password = "JarMad0306#";             // MySQL password (default is empty for XAMPP)
$database = "jarrrwco_CollectAPP";


// Create connection
$conn = new mysqli($host, $username, $password,  $database);

$id = $_GET['id'];
$movie = $_GET['movie'];
$addTimestamp = date('Y-m-d H:i:s');

$stmt = $conn->prepare("INSERT INTO favorites (id, MovieID, addTimestamp)
                        VALUES (?,?,?)");
$stmt->bind_param("iss", $id, $movie, $addTimestamp);
$stmt->execute();

