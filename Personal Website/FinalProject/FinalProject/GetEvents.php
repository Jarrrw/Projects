<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5500"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); 
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json");

$host = "localhost:3306";  // Change if using a remote server
$username = "jarrrwco_2";         // MySQL username
$password = "JarMad0306#";             // MySQL password (default is empty for XAMPP)
$database = "jarrrwco_CalenderApp";

try {
    mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
    $conn = new mysqli($host, $username, $password, $database);

    $userID = $_GET['userID'] ?? '';
    $month = $_GET['month'] ?? '';  // Ensure month is treated as a string
    $year = $_GET['year'] ?? '';    // Ensure year is treated as a string
    
    // Validate userID as an integer
   
    
    // Prepare and execute the SELECT query
    $stmt = $conn->prepare("SELECT * FROM events WHERE userID = ? AND month = ? AND year = ?");
    $stmt->bind_param("iis", $userID, $month, $year);
    $stmt->execute();
    
    // Fetch the result
    $result = $stmt->get_result();
    $events = $result->fetch_all(MYSQLI_ASSOC);
    
    // Free the result
    $result->free();
    
    // Return events as JSON
    echo json_encode($events);
} catch (mysqli_sql_exception $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
