let answer = '';
let img = 1;
let checkbox = document.getElementById('myCheckbox');
function startGame() {
   // Fetch a new word from the server
   fetch('getWord.php')
       .then(response => response.json())
       .then(data => {
           if(data.word) {
                if (checkbox.checked) {
                    alert(data.word);
                }
               setupGame(data.word);
           } else {
               console.error('Error fetching word:', data.error);
           }
       })
       .catch(error => console.error('Error:', error));
}

function setupGame(word) {
    let image = document.getElementById('hangman');
    image.src = 'images/1.png';
    img = 1;
    answer = word; 
   const wordToGuess = document.getElementById('wordToGuess');
   wordToGuess.innerHTML = '_ '.repeat(word.length).trim();
   generateLetterButtons();
}

function generateLetterButtons() {
   const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
   const lettersDiv = document.getElementById('letters');
   lettersDiv.innerHTML = ''; // Clear previous buttons
   letters.split('').forEach(letter => {
       const button = document.createElement('button');
       button.classList.add(letter);
       button.textContent = letter;
       button.onclick = () => guessLetter(letter);
       lettersDiv.appendChild(button);
   });
}

function guessLetter(letter) {
   console.log('Guessed letter:', letter);
   
   // Implement the guessing logic here
   // This is where you would update the displayed word or handle incorrect guesses
   var wordToGuess = document.getElementById('wordToGuess').innerText;
   
   if (answer.includes(letter.toLowerCase())) {
       // Update the displayed words
       var wordToGuessArray = wordToGuess.split(' ');
       console.log(wordToGuess);
        console.log(wordToGuessArray);
        // Loop through all occurrences of the letter in the answer and update the wordToGuessArray
        for (let i = 0; i < answer.length; i++) {
            if (answer[i] === letter.toLowerCase()) {
                wordToGuessArray[i] = letter.toLowerCase();  // Replace the corresponding underscore
            }
        }
        wordToGuess = wordToGuessArray.join(' ');
        document.getElementById('wordToGuess').innerText = wordToGuess;

        document.getElementsByClassName(letter)[0].style.display = 'none';
        if (!wordToGuessArray.includes('_')) {
            alert("You won!");
            startGame();
        }
   }
   else {
         document.getElementsByClassName(letter)[0].style.display = 'none';
         var image = document.getElementById('hangman');
         img++;
         if (img == 12) {
             alert("You lost! The word was: " + answer);
             startGame();
         }
         console.log(img);
         image.src = `images/${img}.png`;
   }
}
checkbox = document.getElementById('myCheckbox');
checkbox.addEventListener('change', function() {
    if (checkbox.checked) {
        alert(answer);
    } 
  });
// Initially start the game
startGame();