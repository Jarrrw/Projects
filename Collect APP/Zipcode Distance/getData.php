<?php
header("Access-Control-Allow-Origin: *"); // Allows any origin
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");


$filePath = "../../SecureFiles/US20Codes20201320Data.txt";
$contents = file_get_contents($filePath);
$query1 = $_GET['zip1'] ?? '';
$query2 = $_GET['zip2'] ?? '';

$lines = explode("\n", $contents);
$results = [];

foreach ($lines as $line) {
    $parts = explode(",", $line);
    if (count($parts) >= 3) {
        $results[$parts[0]] = [
            "zip" => $parts[0],
            "latitude" => (float)$parts[1],
            "longitude" => (float)$parts[2]
        ];
    }
}
header('Content-Type: application/json');
$final = [];
if (isset($results[$query1])) {
    $final[$query1] = $results[$query1];
} else {
    echo json_encode(["error" => "ZIP code not found"]);
}

if (isset($results[$query2])) {
    $final[$query2] = $results[$query2];
} else {
    echo json_encode(["error" => "ZIP code not found"]);
}

echo json_encode($final);

?>