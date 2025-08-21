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
      header("Location: login.html?query=lock" );
      exit();
    }
    $storePassword = $user['password'];
    if (password_verify($password, $storePassword)){
      $numLogin = $user['numLogin'] + 1;
      $updateStmt = $conn->prepare("UPDATE accounts SET numLogin = ?, lastLogin = NOW() WHERE id = ?");
      $updateStmt->bind_param("ii", $numLogin, $user['id']);
      $updateStmt->execute();
      header("Location: http://www.jarrrw.com/CollectAPP/search/search.html?id=" . $user['id']);
    }
    else {
      $numFails += + 1;
      $updateStmt = $conn->prepare("UPDATE accounts SET failedLogin = ? WHERE id = ?");
      $updateStmt->bind_param("ii", $numFails, $user['id']);
      $updateStmt->execute();
      header("Location: login.html?query=nope" );
    }
  
  } else {
    // Invalid credentials
    echo "Invalid email or password.";
    header("Location: login.html?query=no" );
  }
  
  $stmt->close();
  $conn->close();
?>