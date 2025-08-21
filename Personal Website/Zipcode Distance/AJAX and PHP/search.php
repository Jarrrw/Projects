<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");
header("Content-Type: text/xml");

$statesAndCapitals = array(
   "Alabama" => "Montgomery", "Alaska" => "Juneau", "Arizona" => "Phoenix", "Arkansas" => "Little Rock",
   "California" => "Sacramento", "Colorado" => "Denver", "Connecticut" => "Hartford", "Delaware" => "Dover",
   "Florida" => "Tallahassee", "Georgia" => "Atlanta", "Hawaii" => "Honolulu", "Idaho" => "Boise",
   "Illinois" => "Springfield", "Indiana" => "Indianapolis", "Iowa" => "Des Moines", "Kansas" => "Topeka",
   "Kentucky" => "Frankfort", "Louisiana" => "Baton Rouge", "Maine" => "Augusta", "Maryland" => "Annapolis",
   "Massachusetts" => "Boston", "Michigan" => "Lansing", "Minnesota" => "Saint Paul", "Mississippi" => "Jackson",
   "Missouri" => "Jefferson City", "Montana" => "Helena", "Nebraska" => "Lincoln", "Nevada" => "Carson City",
   "New Hampshire" => "Concord", "New Jersey" => "Trenton", "New Mexico" => "Santa Fe", "New York" => "Albany",
   "North Carolina" => "Raleigh", "North Dakota" => "Bismarck", "Ohio" => "Columbus", "Oklahoma" => "Oklahoma City",
   "Oregon" => "Salem", "Pennsylvania" => "Harrisburg", "Rhode Island" => "Providence", "South Carolina" => "Columbia",
   "South Dakota" => "Pierre", "Tennessee" => "Nashville", "Texas" => "Austin", "Utah" => "Salt Lake City",
   "Vermont" => "Montpelier", "Virginia" => "Richmond", "Washington" => "Olympia", "West Virginia" => "Charleston",
   "Wisconsin" => "Madison", "Wyoming" => "Cheyenne"
);

echo "<?xml version=\"1.0\" ?>\n";

echo "<names>\n";
$query = $_GET['query'] ?? '';
foreach ($statesAndCapitals as $state => $capital) {
   if (str_starts_with(strtolower($state), strtolower($query))) {
      echo "<name>$capital</name>\n";
   }
}

echo "</names>\n";

?>