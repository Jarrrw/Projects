<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5500"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); 
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

$host = "localhost:3306";  // Change if using a remote server
$username = "jarrrwco_2";         // MySQL username
$password = "JarMad0306#";             // MySQL password (default is empty for XAMPP)
$database = "jarrrwco_CalenderApp";


try {
  mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
  $conn = new mysqli($host, $username, $password, $database);
  $userID = $_POST['userID'] ?? '';
  $title = $_POST['title'] ?? '';
  $date = new DateTime($_POST['date']) ?? '';
  $startTime = $_POST['start'] ?? '';
  $endTime = $_POST['end'] ?? '';
  $description = $_POST['description'] ?? '';
  $year = $date->format('Y');
  $month = $date->format('m');
  $day = $date->format('d');


  // Prepare and execute
  $stmt = $conn->prepare("INSERT INTO  events ( userID, day, month, year, title, description, startTime, endTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
  $stmt->bind_param("isssssss", $userID, $day, $month, $year, $title, $description, $startTime, $endTime);
  $stmt->execute();

  
  exit();

} catch (mysqli_sql_exception $e) {
  // Log the error or handle gracefully
  error_log("Signup error: " . $e->getMessage());
  
  exit();
}