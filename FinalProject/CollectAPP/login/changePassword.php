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

$security = $_POST['security'];
$email = $_POST['email'];
$password = $_POST['password'];

$stmt = $conn->prepare("SELECT * FROM accounts WHERE email = ? AND security = ?");
$stmt->bind_param("ss", $email, $security);
$stmt->execute(); // both are strings


$result = $stmt->get_result();
if ($result->num_rows > 0) {
  // User exists — valid login
  $user = $result->fetch_assoc();
  $numFails = 0;
  $updateStmt = $conn->prepare("UPDATE accounts SET failedLogin = ? WHERE id = ?");
  $updateStmt->bind_param("ii", $numFails, $user['id']);
  $updateStmt->execute();
}
else {
  header("Location: password.html?query=si" );
  exit();
}

$newPassword = password_hash($password, PASSWORD_DEFAULT);
$stmt = $conn->prepare("UPDATE accounts set password = ? where email = ? AND security = ?");
$stmt->bind_param("sss",$newPassword, $email, $security);
$stmt->execute();
if ($stmt->affected_rows > 0) {
    header("Location: https://www.jarrrw.com\FinalProject\login\login.html?query=si" );
  } else {
    header("Location: https://www.jarrrw.com\FinalProject\login\password.html?query=no" );
  }
