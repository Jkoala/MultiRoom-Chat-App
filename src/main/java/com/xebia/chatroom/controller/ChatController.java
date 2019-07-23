package com.xebia.chatroom.controller;

import static java.lang.String.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.xebia.chatroom.dao.ChatRoomDao;
import com.xebia.chatroom.dao.RoomsUserDao;
import com.xebia.chatroom.dao.UserRoomsDao;
import com.xebia.chatroom.model.ChatMessage;
import com.xebia.chatroom.model.ChatMessage.MessageType;

/**
 * @author heenanagpal
 *
 */
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private ChatRoomDao chatRoomDao;

    @Autowired
    private UserRoomsDao userRoomsDao;

    @Autowired
    private RoomsUserDao roomsUserDao;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    }

    @MessageMapping("/chat/{roomId}/addUser")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
        if (currentRoomId != null) {
            ChatMessage leaveMessage = new ChatMessage();
            leaveMessage.setType(MessageType.LEAVE);
            leaveMessage.setSender(chatMessage.getSender());
            messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
            userRoomsDao.removeRoom(chatMessage.getSender(), roomId);
            roomsUserDao.removeUsername(roomId, chatMessage.getSender());
        } else {
            chatRoomDao.addRoom(chatMessage.getSender(), roomId);
            userRoomsDao.addRoom(chatMessage.getSender(), roomId);
            roomsUserDao.addRoom(roomId, chatMessage.getSender());
        }

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    }
}
