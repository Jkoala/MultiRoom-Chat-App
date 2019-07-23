package com.xebia.chatroom.controller;

import static java.lang.String.format;

import com.xebia.chatroom.dao.ChatRoomDao;
import com.xebia.chatroom.dao.RoomsUserDao;
import com.xebia.chatroom.dao.UserRoomsDao;
import com.xebia.chatroom.model.ChatMessage;
import com.xebia.chatroom.model.ChatMessage.MessageType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author heenanagpal
 *
 */
@Component
public class WebSocketEventListener {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
  
  @Autowired
  private UserRoomsDao userRoomsDao;

  @Autowired
  private RoomsUserDao roomsUserDao;

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    logger.info("Received a new web socket connection.");
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");
    if (username != null) {
      logger.info("User Disconnected: " + username);

      ChatMessage chatMessage = new ChatMessage();
      chatMessage.setType(MessageType.LEAVE);
      chatMessage.setSender(username);

      messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
      
      userRoomsDao.removeRoom(chatMessage.getSender(), roomId);
      roomsUserDao.removeUsername(roomId, chatMessage.getSender());
    }
  }
}
