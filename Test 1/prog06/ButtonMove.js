let count = 0;
let dir = {};
let id = 0;
function makeButton() {
    var view = document.getElementById("view");
    let dropdown = document.getElementById("list");

    label = Math.floor(Math.random() * 99) + 1

    let button = document.createElement("button");
    button.innerText = label;
    button.style.position = "absolute";
    button.addEventListener("click", function () {
        button.style.background = dropdown.options[dropdown.selectedIndex].text;
        count += Number(button.innerHTML);
        document.getElementById("count").innerText = count;
    })
    button.style.background = dropdown.options[dropdown.selectedIndex].text;
    button.style.width = "30px";
    button.style.height = "20px";
    button.classList.add("mover");
    button.id = id.toString();
    console.log(button.id);
    dir[id] = Math.floor(Math.random() * 4) + 1;
    id++;
  
    let xPosition = Math.floor(Math.random() * (view.clientWidth - 30));
    let yPosition = Math.floor(Math.random() * (view.clientHeight - 20));
    button.style.left = `${xPosition}px`;
    button.style.top = `${yPosition}px`;

    dir[button] = Math.floor(Math.random() * 2) + 1;
    view.appendChild(button);
}


let isMoving = false;
let interval;

function updateButtonsList() {
    buttons = document.querySelectorAll(".mover"); // Select all buttons again
}

function moveButtons() {
    document.querySelectorAll(".mover").forEach(button => {
        if (dir[button.id] == '1') {
            MoveRight(button);
        }
        else if (dir[button.id] == '2') {
            MoveLeft(button);
        }
        else if (dir[button.id] == '3') {
            MoveDown(button);
        }
        else if (dir[button.id] == '4') {
            MoveUp(button);
        }
    });
}

function MoveRight(button) {
    var view = document.getElementById("view");
    var currentLeft = parseInt(window.getComputedStyle(button).left, 10) || 0;
    if (currentLeft + 5 > view.clientWidth - 30) {
        dir[button.id] = "2";
        MoveLeft(button);
    }
    else {
        button.style.left = (currentLeft + 5) + "px";
    }
}

function MoveLeft(button) {
    var view = document.getElementById("view");
    var currentLeft = parseInt(window.getComputedStyle(button).left, 10) || 0;
    if (currentLeft - 5 < 0) {
        dir[button.id] = "1";
        MoveRight(button);
    }
    else {
        button.style.left = (currentLeft - 5) + "px";
    }
}

function MoveUp(button) {
    var view = document.getElementById("view");
    var currentTop = parseInt(window.getComputedStyle(button).top, 10) || 0;
    if (currentTop - 5 < 0) {
        dir[button.id] = "3";
        MoveDown(button);
    }
    else {
        button.style.top = (currentTop - 5) + "px";
    }
}

function MoveDown(button) {
    var view = document.getElementById("view");
    var currentTop = parseInt(window.getComputedStyle(button).top, 10) || 0;
    if (currentTop + 5 > view.clientHeight - 20) {
        dir[button.id] = "4";
        MoveUp(button);
    }
    else {
        button.style.top = (currentTop + 5) + "px";
    }
}


function toggleMovement() {
    if (isMoving) {
        button = document.getElementById("start");
        button.innerText = "Move"
        clearInterval(interval); // Stop moving
        isMoving = false;
    } else {
        button = document.getElementById("start");
        button.innerText = "Pause"
        interval = setInterval(moveButtons, 100); // Start moving every 100ms
        isMoving = true;
    }
}