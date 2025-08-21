<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); 
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

$host = "localhost:3306";  // Change if using a remote server
$username = "jarrrwco_2";         // MySQL username
$password = "JarMad0306#";             // MySQL password (default is empty for XAMPP)
$database = "jarrrwco_CalenderApp";


// Create connection
$conn = new mysqli($host, $username, $password,  $database);

$email = $_POST['email'];
$password = $_POST['password'];

$stmt = $conn->prepare("SELECT * FROM accounts WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute(); // both are strings



$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // User exists — valid login
    $user = $result->fetch_assoc();
    $numFails = $user['failedLogin'];
    if ($numFails >= 3) {
      header("Location: https://www.jarrrw.com/FinalProject/login/login.html?query=lock" );
      exit();
    }
    $storePassword = $user['password'];
    if (password_verify($password, $storePassword)){
      
      $numLogin = $user['numLogin'] + 1;
      $failedLogin = 0;
      $updateStmt = $conn->prepare("UPDATE accounts SET numLogin = ?, lastLogin = NOW() WHERE id = ?");
      $updateStmt->bind_param("ii", $numLogin, $user['id']);
      $updateStmt->execute();
      $updateStmt = $conn->prepare("UPDATE accounts SET failedLogin = ?, lastLogin = NOW() WHERE id = ?");
      $updateStmt->bind_param("ii", $failedLogin, $user['id']);
      $updateStmt->execute();
      header( "Location: https://www.jarrrw.com/FinalProject/showcalendar_inJS.html?id=" . $user['id']);
      echo "Password Correct". $user['id'];
      exit();
    }
    else {
      $numFails += + 1;
      $updateStmt = $conn->prepare("UPDATE accounts SET failedLogin = ? WHERE id = ?");
      $updateStmt->bind_param("ii", $numFails, $user['id']);
      $updateStmt->execute();
      header("Location: https://www.jarrrw.com/FinalProject/login/login.html?query=nope" );
    }
  
  } else {
    // Invalid credentials
    echo "Invalid email or password.";
    header("Location: https://www.jarrrw.com/FinalProject/login/login.html?query=no" );
  }
  
  $stmt->close();
  $conn->close();
?>