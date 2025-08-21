<?php 
    session_regenerate_id(true);

    $username = htmlspecialchars($_POST["username"], ENT_QUOTES, 'UTF-8');

    setcookie("user_session", session_id(), [
        "expires" => time() + 3600,
        "path" => "/",
        "domain" => "",
        "secure" => true,
        "httponly" => true,
        "samesite" => "Lax"

    ]);

    header("Location: dashboard.php");
?>