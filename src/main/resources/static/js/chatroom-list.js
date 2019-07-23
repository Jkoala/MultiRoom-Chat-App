'use strict';

var roomIdInput = $('#room-id');
var chatRoomForm = document.querySelector('#chatRoomForm');

var stompClient = null;
var currentSubscription;
var username = null;
var roomId = null;
var topic = null;

function connect(event) {
	roomId = roomIdInput.val().trim();
	Cookies.set('roomId', roomId);
	event.preventDefault();
    window.location = "/chatwindow.html";
}

function joinChatRoom(roomId, username) {
	Cookies.set('roomId', roomId);
	event.preventDefault();
    window.location = "/chatwindow.html";
}

$(document).ready(function() {
	chatRoomForm.addEventListener('submit', connect, true);
	var listUserGroups = [];
	var savedName = Cookies.get('name');
	$("#username").append(savedName)
	$.getJSON("http://localhost:8080/user/room/list?username="+savedName, function(result) {
		$.each(result, function(i, field) {
			listUserGroups.push(field);
			$("#list-of-groups").append("<li class='list-group-item active'>" + field + "<button class='btn btn-danger' style='float:right'>LEAVE</button></li>");
		});
	});
	
	$.getJSON("http://localhost:8080/all/room/list", function(result) {
		$.each(result, function(i, field) {
			if (!listUserGroups.includes(i)) {
					$("#list-of-groups").append(
						"<li class='list-group-item'>" 
						+ i 
						+ "<button class='btn btn-primary' style='float:right' onClick=\"joinChatRoom('" + i + "','" + savedName + "')\">JOIN...</button>"
						/* if (field == savedName) {
							+ "<button class='btn btn-primary' style='float:right'>DELETE...</button>"								  
						} */
						+ "</li>"
					);
			}
		});
	});
});
