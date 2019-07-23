'use strict';

var nameInput = $('#name');
var usernameForm = document.querySelector('#usernameForm');
var username = null;

function connect(event) {
	username = nameInput.val().trim();
	Cookies.set('name', username);
	event.preventDefault();
    window.location = "/chatroom-list.html";
}

$(document).ready(function() {
	usernameForm.addEventListener('submit', connect, true);
});
