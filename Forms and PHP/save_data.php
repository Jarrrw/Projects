<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
  // Get the posted data which came from client
  $name = isset($_POST['name']) ? $_POST['name'] : '';
  $age = isset($_POST['age']) ? $_POST['age'] : '';
  $birthday = isset($_POST['birthday']) ? $_POST['birthday'] : '';
  $job = isset($_POST['job']) ? $_POST['job'] : '';
  $animal = isset($_POST['animal']) ? $_POST['animal'] : '';
  $personType = isset($_POST['personType']) ? $_POST['personType'] : '';
  $season = isset($_POST['season']) ? $_POST['season'] : '';
  $hobby = isset($_POST['hobby']) ? $_POST['hobby'] : '';
  $introvert = isset($_POST['introvert']) ? $_POST['introvert'] : '';
  $genre = isset($_POST['genre']) ? $_POST['genre'] : '';

  // prepare data to write to a text file using JSON
  $file = "data.txt";
  $data = array(
    'name' => $name, 
    'age' => $age, 
    'birthday' => $birthday,
    'job' => $job,
    'animal' => $animal,
    'personType' => $personType,
    'season' => $season,
    'hobby' => $hobby,
    'introvert' => $introvert,
    'genre' => $genre
);

  // Encode the updated data array to JSON
  $json_data = json_encode($data);

  // Write the JSON data back to the file
  file_put_contents($file, $json_data . PHP_EOL);

  // Redirect back to the index page
  header("Location: index.html");
  exit();
} else
    echo "Invalid request!";
?>