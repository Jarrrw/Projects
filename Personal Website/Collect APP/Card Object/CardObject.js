let cards = [];

var tom = new Card("Tom Jones", "tom@jones.com",
    "123 Elm Street, Sometown ST 77777",
    "555-555-9876", "2019-12-01");
cards.push(tom);

var holmes = new Card();
holmes.name = "Sherlock Holmes";
holmes.email = "sherlock@holmes.com";
holmes.address = "221B Baker Street";
holmes.phone = "555-555-3456";
holmes.birthday = "1854-01-06"
cards.push(holmes);

var sue = new Card("Sue Suthers", "sue@suthers.com", "123 Elm Street,Yourtown ST 99999", "555-555-9876", "1989-9-29");
cards.push(sue);
function submitForm() {
    var user = document.getElementById("name").value;
    var email = document.getElementById("email").value;
    var address = document.getElementById("address").value;
    var phone = document.getElementById("phone").value;
    var birthday = document.getElementById("birthday").value;
    var card = new Card(user, email, address, phone, birthday);
    card.printCard();
}

function printAll() {
    cards.forEach(card => {
        card.printCard()
    });
}
function printCard() {
    var nameLine = "<strong>Name: </strong>" + this.name + "<br>";
    var emailLine = "<strong>Email: </strong>" + this.email + "<br>";
    var addressLine = "<strong>Address: </strong>" + this.address + "<br>";
    var phoneLine = "<strong>Phone: </strong>" + this.phone + "<br>";
    var birthdayLine = "<strong>Birthday: </strong>" + this.birthday + "<hr>";
    var card = document.getElementById("card")
    card.innerHTML = card.innerHTML + nameLine + emailLine + addressLine + phoneLine + birthdayLine;
 }

 function Card(name,email,address,phone,birthday) {
    this.name = name;
    this.email = email;
    this.address = address;
    this.phone = phone;
    this.birthday = birthday
    this.printCard = printCard;
 }