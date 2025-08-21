// initialize the counter and the array
var numbernames=0;
var names = new Array();
function SortNames() {
   // Get the name from the text field
   thename=document.theform.newname.value;
   thename = thename.charAt(0).toUpperCase() + thename.slice(1);
   // Add the name to the array
   names[numbernames]=thename;
   // Increment the counter
   numbernames++;
   // Sort the array
   names.sort();
   let orderedList = names.map((name, index) => `${index + 1}. ${name}`).join("\n");

   document.theform.sorted.value = orderedList;
}

document.addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        event.preventDefault();
        SortNames();
    }
})