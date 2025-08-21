

document.addEventListener("DOMContentLoaded", () => {
    
    function slideBlock() {
        document.getElementById("animatedBlock").classList.toggle("animate");
    }

    const draggable = document.getElementById("draggable");

    let offsetX, offsetY, isDragging = false;

    draggable.addEventListener("mousedown", (e) => {
        isDragging = true;
        offsetX = e.clientX - draggable.offsetLeft;
        offsetY = e.clientY - draggable.offsetTop;
        draggable.style.cursor = "grabbing";
    });

    document.addEventListener("mousemove", (e) => {
        if (isDragging) {
            draggable.style.left = `${e.clientX - offsetX}px`;
            draggable.style.top = `${e.clientY - offsetY}px`;
        }
    });

    document.addEventListener("mouseup", () => {
        isDragging = false;
        draggable.style.cursor = "grab";
    });

    // Attach slideBlock function to button
    document.getElementById("slide").addEventListener("click", slideBlock);


});
function onInput() {
    const nameInput = document.getElementById("name");
    const response = document.getElementById("response");
    
    response.textContent = `Hello, ${nameInput.value}! 👋`;

}

function changeBackground() {
    var button = document.getElementById("mouseClick");
    if (button.style.backgroundColor == "blue") {
        button.style.backgroundColor = "black";
    }
    else {
        button.style.backgroundColor = "blue";
    }
}

function changeBorder() {

    var input = document.getElementById("focus");
    input.style.borderWidth = "10px";
    input.style.borderColor = "blue";
}