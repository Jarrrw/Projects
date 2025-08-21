<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
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

  $name     = $_POST['fullname'] ?? '';
  $email    = $_POST['email'] ?? '';
  $password = $_POST['password'] ?? '';
  $security = $_POST['question'] ?? '';

  // Hash the password
  $password = password_hash($password, PASSWORD_DEFAULT);

  // Prepare and execute
  $stmt = $conn->prepare("INSERT INTO accounts (name, email, password, security) VALUES (?, ?, ?, ?)");
  $stmt->bind_param("ssss", $name, $email, $password, $security);
  $stmt->execute();

  header("Location: https://www.jarrrw.com/FinalProject/login/login.html?query=yes");
  exit();

} catch (mysqli_sql_exception $e) {
  // Log the error or handle gracefully
  error_log("Signup error: " . $e->getMessage());
  header("Location: https://www.jarrrw.com/FinalProject/login/signup.html?query=si");
  exit();
}