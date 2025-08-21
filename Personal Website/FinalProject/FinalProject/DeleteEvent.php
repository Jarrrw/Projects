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

    $eventID = $_GET['id'] ?? '';   
    
   
    
    // Prepare and execute the SELECT query
    $stmt = $conn->prepare("DELETE FROM events WHERE eventID = ?");
    $stmt->bind_param("i", $eventID);
    $stmt->execute();
    
    // Fetch the result
    
    
    // Free the result
    $result->free();
    
} catch (mysqli_sql_exception $e) {
    echo json_encode(['error' => $e->getMessage()]);
}
