package com.xebia.chatroom.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.xebia.chatroom.dao.RoomsUserDao;
import com.xebia.chatroom.dao.UserRoomsDao;
import com.xebia.chatroom.model.ChatMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ChatControllerTest {

    @MockBean
    private ChatController chatController;

    @MockBean
    private UserRoomsDao userRoomsDao;

    @MockBean
    private RoomsUserDao roomsUserDao;

    @MockBean
    private SimpMessageHeaderAccessor simpMessageHeaderAccessor;
    
    @Test
    public void testSendMessage() throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("content");
        chatMessage.setSender("username");
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatController.sendMessage("roomId", chatMessage);
    }
    
    @Test
    public void testJoinChatRoom() throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("content");
        chatMessage.setSender("username");
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatController.addUser("roomId", chatMessage, simpMessageHeaderAccessor);
    }
    
    @Test
    public void testLeaveChatRoom() throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("content");
        chatMessage.setSender("username");
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatController.addUser("roomId", chatMessage, simpMessageHeaderAccessor);
    }

}
