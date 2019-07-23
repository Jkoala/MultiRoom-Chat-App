package com.xebia.chatroom.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author heenanagpal
 *
 */
@Repository
public class UserRoomsDao {
    private static final Logger logger = LoggerFactory.getLogger(UserRoomsDao.class);

    private static Map<String, List<String>> userRoomList = new HashMap<String, List<String>>();

    /**
     * Method used to add room for the user.
     * @param username
     * @param chatroom
     */
    public void addRoom(String username, String chatroom){
        if(userRoomList.containsKey(username)) {
            List<String> rooms = userRoomList.get(username);
            rooms.add(chatroom);
        } else {
            List<String> rooms = new ArrayList<String>();
            rooms.add(chatroom);
            userRoomList.put(username, rooms);
        }
    }
    
    /**
     * Method used to remove user from the list
     * @param username
     */
    public void removeUser(String username){
        if(userRoomList.containsKey(username)) {
            userRoomList.remove(username);
        } else {
            logger.error("No user to remove.");
        }
    }
    
    /**
     * Method used to remove room from the store.
     * @param username
     * @param chatroom
     */
    public void removeRoom(String username, String chatroom){
        if(userRoomList.containsKey(username)) {
            List<String> rooms = userRoomList.get(username);
            rooms.remove(chatroom);
            if(rooms.size() == 0) {
                userRoomList.remove(username);
            }
        } else {
            logger.error("No char room given to user.");
        }
    }
    
    /**
     * Method used to remove room from the store.
     * @param username
     * @param chatroom
     */
    public List<String> userChatRooms(String username){
        if(userRoomList.containsKey(username)) {
            List<String> rooms = userRoomList.get(username);
            return rooms;
        } else {
            return new ArrayList<String>();
        }
    }
}
