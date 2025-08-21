<?php
// Allow requests from any origin (Change "*" to a specific origin if needed)
header("Access-Control-Allow-Origin: *");

// Allow specific HTTP methods (GET, POST, etc.)
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");

// Allow specific headers
header("Access-Control-Allow-Headers: Content-Type, Authorization");
// Read JSON data from the text file
$file = "data.txt";
$json_data = file_get_contents($file);

// Decode JSON data into an associative array
$data_array = json_decode($json_data, true);

// Extract name and age from the data
$name = isset($data_array['name']) ? $data_array['name'] : '';
$age = isset($data_array['age']) ? $data_array['age'] : '';
$birthday = isset($data_array['birthday']) ? $data_array['birthday'] : '';
$job = isset($data_array['job']) ? $data_array['job'] : '';
$animal = isset($data_array['animal']) ? $data_array['animal'] : '';
$personType = isset($data_array['personType']) ? $data_array['personType'] : '';
$season = isset($data_array['season']) ? $data_array['season'] : '';
$hobby = isset($data_array['hobby']) ? $data_array['hobby'] : '';
$introvert = isset($data_array['introvert']) ? $data_array['introvert'] : '';
$genre = isset($data_array['genre']) ? $data_array['genre'] : '';

// Create an associative array to hold the extracted data
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

// Send the data back to the client as JSON
header('Content-Type: application/json');
echo json_encode($data);
?>
