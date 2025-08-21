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


$stmt = $conn->prepare("SELECT * FROM favorites WHERE id = ?");
$stmt->bind_param("i", $id);

$stmt->execute();

$result = $stmt->get_result();
$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);

