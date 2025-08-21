/* This hashmap keeps track of the amount of times each the background
is changed to the corresponding color*/ 
let count = {};
count['red'] = 0;
count['blue'] = 0;
count['green'] = 0;
/* The hashmap that keep track the amount of times each button labeled
to the corresponding color changes colors*/
let change = {};
change['red'] = 0;
change['blue'] = 0;
change['green'] = 0;


/* This function changes the background color of the webpage 
to the color specified in the parameter */
function changeColor(color) {
    // Increments counter of the new color of the background
    count[color]++;
    // Changes the background of the web application
    document.body.style.backgroundColor = color;
    // Gets the counter object from the html document
    let counter = document.getElementById(color + "_counter")
    // Sets text on the webpage to the newly incremented counter
    counter.textContent = count[color];
}

/* This function reverses the colors of the buttons*/
function switchColor(color) {
    // Increments the counter that corresponds to the color passed in as a parameter
    change[color]++;
    // Gets the button from the html based on the color id of the object
    button = document.getElementById(color);
    // Changes the background of the button
    button.style.backgroundColor = "white";
    // Changes the label color of the button
    button.style.color = "black";
    // Gets the counter from the html based on the color
    let counter = document.getElementById(color + "_" + "change")
    // Sets counter text on the webpage to the newly incremented counter
    counter.textContent = change[color];
}
/* This function reverses the color back to the original*/
function switchBack(color) {
    // Gets the button element by the color
    button = document.getElementById(color);
    // Sets the background back to black
    button.style.backgroundColor = "black";
    // Sets the font color of the button back to white
    button.style.color = "white";
}