// Jarrett Wilson
// 2/20/2025
// CPSC 3750
// Programming Exam #1
// Grade level you completed: A


let prime = [];
let nonprime = [];

document.getElementById("generateButton").addEventListener('click', (event) => {
    event.preventDefault();
    var input = document.getElementById("input");
    var num = input.value;
    if (num < 0) {
        alert("You cannot enter a negative")
    }
    else if (Number.isInteger(num)) {
        alert("You cannot enter a decimal number")
    }
    generateNonPrimeList(num);
    generatePrimeList(num);
})

function isPrime(num) {
    if (num == 1) {
        return false;
    }
    var cnt = 0;
    for (var i = 1; i <= num; i++) {

        // Check how many number is divisible
        // by n
        if (num % i == 0)
            cnt++;
    }
    // If n is divisible by more than 2 numbers
    // then it is not prime
    if (cnt > 2)
        return false;

    // else it is prime
    else
        return true;
}
function generatePrimeList(limit) {
    
    prime = [];
    var list = document.getElementById("prime")
    list.innerHTML = "";
    for (var i = 1; i <= limit; i++) {
        if (isPrime(i)) {
            prime.push(i);
            const li = document.createElement('li');
            li.textContent = i;
            
            list.appendChild(li);
        }
    }

}
function generateNonPrimeList(limit) {
    nonprime = [];
    var list = document.getElementById("nonprime")
    list.innerHTML = "";
    for (var i = 1; i <= limit; i++) {
        if (!isPrime(i)) {
            nonprime.push(i);
            const li = document.createElement('li');
            li.textContent = i;
            
            list.appendChild(li);
        }
    }
}

document.getElementById('reversePrime').addEventListener('click', function() {
    const prime = document.getElementById('prime');
    const primeItems = Array.from(prime.getElementsByTagName('li'));
    
    // Reverse the order of the array
    primeItems.reverse();  
    // Clear the list
    prime.innerHTML = '';
  
    // Append the items in reverse order
    primeItems.forEach(item => prime.appendChild(item));
});

document.getElementById('reverseNon').addEventListener('click', function() {
    const nonprime = document.getElementById('nonprime');
    const nonPrimeItems = Array.from(nonprime.getElementsByTagName('li'));  // Convert the list items into an array
    
    // Reverse the order of the array
    nonPrimeItems.reverse();
  
    // Clear the list
    nonprime.innerHTML = '';
  
    // Append the items in reverse order
    nonPrimeItems.forEach(item => nonprime.appendChild(item));
});

let toggleButton = document.getElementById("darkmode");
toggleButton.addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    // Change button text based on the current mode
    if (document.body.classList.contains('dark-mode')) {
      toggleButton.textContent = 'Switch to Light Mode';
    } else {
      toggleButton.textContent = 'Switch to Dark Mode';
    }
});

let sumPrime = document.getElementById("sumPrime")
sumPrime.addEventListener('click', () => {
    var sum = 0;
    for (i = 0; i < prime.length; i++) {
        sum += prime[i];
    }
    sumPrime.innerText = sum;
})

let nonPrime = document.getElementById("sumNon");
nonPrime.addEventListener('click', () => {
    var sum = 0;
    console.log(nonprime);
    for (i = 0; i < nonprime.length; i++) {
        sum += nonprime[i];
    }
    nonPrime.innerText = sum;
})