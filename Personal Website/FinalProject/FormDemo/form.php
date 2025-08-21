<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");


echo "My name is " . $_POST['name'] . "<br>";
echo "<br>";
echo "The answer to the text area was: <br> " . $_POST['summer'] . "<br>";
echo "<br>";
echo $_POST['hidden'] . "<br>";
echo "<br>";
echo "My password is " . $_POST['password'] . "<br>";
echo "<br>";
echo "Checkbox question answer: <br>";
$i = 1;
foreach ($_POST['some_statements'] as $answer) {
    echo $i . ". " . $answer. "<br>";
    $i++;
}
echo "<br>";
echo "Radio Question Answer: " . $_POST['best_thing'] . "<br>";
echo "<br>";
echo "Selection List Answer: " . $_POST['how_improve'] . "<br>";
echo "<br>";
if ($_FILES["file"]["error"] !== 0) {
    echo "File upload error: " . $_FILES["file"]["error"];
}
$file = $_FILES['file'];
echo "File Name: " . $file["name"] . "<br>";
echo "File Type: " . $file["type"] . "<br>";
echo "File Size: " . $file["size"] . "<br>";
echo "<br>";
echo "URL: " . $_POST['url'] . "<br>"

?>