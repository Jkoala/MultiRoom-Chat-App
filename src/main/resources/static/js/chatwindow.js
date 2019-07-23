'use strict';

var nameInput = $('#name');
var roomInput = $('#room-id');
var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var roomIdDisplay = document.querySelector('#room-id-display');

var stompClient = null;
var currentSubscription;
var username = null;
var roomId = null;
var topic = null;

var colors = [ '#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107',
		'#ff85af', '#FF9800', '#39bbb0' ];

function connect(event) {
	username = Cookies.get('name');
	roomId = Cookies.get('roomId');
	if (username) {
		chatPage.classList.remove('hidden');

		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, onConnected, onError);
	}
	//event.preventDefault();
}

// Leave the current room and enter a new one.
function enterRoom(newRoomId) {
	roomId = newRoomId;
	Cookies.set('roomId', roomId);
	roomIdDisplay.textContent = roomId;
	topic = `/app/chat/${newRoomId}`;

	if (currentSubscription) {
		currentSubscription.unsubscribe();
	}
	currentSubscription = stompClient.subscribe(`/channel/${roomId}`,
			onMessageReceived);

	stompClient.send(`${topic}/addUser`, {}, JSON.stringify({
		sender : username,
		type : 'JOIN'
	}));
}

function onConnected() {
	enterRoom(roomId);
	connectingElement.classList.add('hidden');
	showUserList();
}

function showUserList(){
	$('#action_menu_btn').click(function() {
		$('.action_menu').toggle();
	});
	var roomId = Cookies.get('roomId');
	$("#chatRoomName").append(roomId)
	$.getJSON("http://localhost:8080/room/user/list?chatroom="+roomId, function(result) {
		$.each(result, function(i, field) {
			$("#contacts").append(
					"<li>" 
					+ "<div class='d-flex bd-highlight'>"
					+ "<div class='user_info'>"	
					+ "<span>"+field+"</span>"
					+ "</div>"
					+ "</div>"
					+"</li>");
		});
	});
}

function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();
	if (messageContent.startsWith('/join ')) {
		var newRoomId = messageContent.substring('/join '.length);
		enterRoom(newRoomId);
		while (messageArea.firstChild) {
			messageArea.removeChild(messageArea.firstChild);
		}
	} else if (messageContent.startsWith('/leave ')) {
		var newRoomId = messageContent.substring('/leave '.length);
		leaveRoom(newRoomId);
		while (messageArea.firstChild) {
			messageArea.removeChild(messageArea.firstChild);
		}
	} else if (messageContent && stompClient) {
		var chatMessage = {
			sender : username,
			content : messageInput.value,
			type : 'CHAT'
		};
		stompClient.send(`${topic}/sendMessage`, {}, JSON
				.stringify(chatMessage));
	}
	messageInput.value = '';
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);

	var messageElement = document.createElement('li');

	if (message.type == 'JOIN') {
		messageElement.classList.add('event-message');
		message.content = message.sender + ' joined!';
	} else {
		messageElement.classList.add('chat-message');

		var avatarElement = document.createElement('i');
		var avatarText = document.createTextNode(message.sender[0]);
		avatarElement.appendChild(avatarText);
		avatarElement.style['background-color'] = getAvatarColor(message.sender);

		messageElement.appendChild(avatarElement);

		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
	}

	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);

	messageElement.appendChild(textElement);

	messageArea.appendChild(messageElement);
	messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}
	var index = Math.abs(hash % colors.length);
	return colors[index];
}

$(document).ready(function() {
	var savedName = Cookies.get('name');
	if (savedName) {
		nameInput.val(savedName);
	}

	var savedRoom = Cookies.get('roomId');
	if (savedRoom) {
		roomInput.val(savedRoom);
	}

	connect();
	messageForm.addEventListener('submit', sendMessage, true);
	
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
