<?php 
    session_start();

    if (!isset($_SESSION["csrf_token"])) {
        $_SESSION["csrf_token"] = bin2hex(random_bytes(32));
    }
    $person = array(
        "name" => "admin",
        "username" => "admin",
        "password" => "password123"
    );
        
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        $username = $_POST['username']; // Get the input value
        $password = $_POST['password'];

        if ($username == "" && $password == "") {
            echo "Username and Password required";
        }
        else if ($username == "") {
            echo "Username required";
        }
        else if ($password == "") {
            echo "Password required";
        }
        
        if ($username == $person['username'] && $password == $person['password']) {
            $_SESSION['name'] = $person['name'];
            
            header("Location: secure_session.php");
            exit();
        }
        else {
            echo "Invalid Credentials";
        }

    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log In</title>
</head>
<body>
    <form method="POST">
        Username: <input type="text" name="username">
        Password: <input type="text" name="password" ?>
        <input type="hidden" name="csrf_token" value="<?php echo $_SESSION['csrf_token']; ?>">
        <input type="submit" value="Log In">
    </form>
</body>
</html>