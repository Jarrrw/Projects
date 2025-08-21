<?php
header("Access-Control-Allow-Origin: http://127.0.0.1:5501"); // MUST match exactly
header("Access-Control-Allow-Credentials: true"); // <-- This is the missing piece!
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
$query = isset($_GET['query']) ? $_GET['query'] : '';

$curl = curl_init();

$encoded_query = urlencode($query);

if (strlen($query) == 0) {
    curl_setopt_array($curl, [
        CURLOPT_URL => "https://imdb236.p.rapidapi.com/imdb/search?rows=100&sortOrder=ASC&sortField=id",
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
}
else 
{
    curl_setopt_array($curl, [
	    CURLOPT_URL => "https://imdb236.p.rapidapi.com/imdb/search?primaryTitle={$encoded_query}&rows=100&sortOrder=ASC&sortField=id",
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
}
$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
	echo "cURL Error #:" . $err;
} else {
	echo $response;
}
$curl = curl_init();