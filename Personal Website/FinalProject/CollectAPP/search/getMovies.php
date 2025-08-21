<?php
// Allow requests from any origin (or specify your frontend origin)
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); // <-- This is the missing piece!
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

// Set API credentials
 // Replace with your actual API key

// Get parameters from frontend request
$query = isset($_GET['query']) ? $_GET['query'] : '';


// Build full API request URL

// Initialize cURL session
$curl = curl_init();

$encoded_query = urlencode($query);

curl_setopt_array($curl, [
	CURLOPT_URL => "https://imdb236.p.rapidapi.com/imdb/autocomplete?query={$encoded_query}",
	CURLOPT_RETURNTRANSFER => true,
	CURLOPT_ENCODING => "",
	CURLOPT_MAXREDIRS => 10,
	CURLOPT_TIMEOUT => 30,
	CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
	CURLOPT_CUSTOMREQUEST => "GET",
	CURLOPT_HTTPHEADER => [
		"x-rapidapi-host: imdb236.p.rapidapi.com",
		"x-rapidapi-key: b3f53007eamsh59fb56d174aca31p168e1ejsn06787a9f2308"
	],
]);

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
	echo "cURL Error #:" . $err;
} else {
	echo $response;
}
?>