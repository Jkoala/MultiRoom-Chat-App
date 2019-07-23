package com.xebia.chatroom.controller;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.xebia.chatroom.dao.ChatRoomDao;
import com.xebia.chatroom.dao.RoomsUserDao;
import com.xebia.chatroom.dao.UserRoomsDao;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class RoomsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @MockBean
    private ChatRoomDao chatRoomDao;
    
    @MockBean
    private UserRoomsDao userRoomsDao;
    
    @MockBean
    private RoomsUserDao roomsUserDao;

    @Test
    public void testAllRoomsList() throws Exception {
        given(this.chatRoomDao.roomList()).willReturn(new HashMap<String, String>());
        @SuppressWarnings("unchecked")
        Map<String, String> response = this.restTemplate.getForObject("/all/room/list", Map.class);
        assertEquals(new HashMap<String, String>(), response);
    }
    
    @Test
    public void testUserRoomList() throws Exception {
        given(this.userRoomsDao.userChatRooms("testUser")).willReturn(new ArrayList<String>());
        @SuppressWarnings("unchecked")
        List<String> response = this.restTemplate.getForObject("/user/room/list?username=testUser", List.class);
        assertEquals(new ArrayList<String>(), response);
    }
    
    @Test
    public void testRoomUserList() throws Exception {
        given(this.userRoomsDao.userChatRooms("testchatroom")).willReturn(new ArrayList<String>());
        @SuppressWarnings("unchecked")
        List<String> response = this.restTemplate.getForObject("/room/user/list?chatroom=testchatroom", List.class);
        assertEquals(new ArrayList<String>(), response);
    }
}