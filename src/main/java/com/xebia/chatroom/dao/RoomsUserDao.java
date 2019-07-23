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
public class RoomsUserDao {
    private static final Logger logger = LoggerFactory.getLogger(RoomsUserDao.class);

    private static Map<String, List<String>> roomUserList = new HashMap<String, List<String>>();

    /**
     * Method used to add room for the user.
     * @param username
     * @param chatroom
     */
    public void addRoom(String chatroom, String username){
        if(roomUserList.containsKey(chatroom)) {
            List<String> usernames = roomUserList.get(chatroom);
            usernames.add(username);
        } else {
            List<String> usernames = new ArrayList<String>();
            usernames.add(username);
            roomUserList.put(chatroom, usernames);
        }
    }
    
    /**
     * Method used to remove user from the list
     * @param username
     */
    public void removeRoom(String chatroom){
        if(roomUserList.containsKey(chatroom)) {
            roomUserList.remove(chatroom);
        } else {
            logger.error("No room to remove..");
        }
    }
    
    /**
     * Method used to remove room from the store.
     * @param username
     * @param chatroom
     */
    public void removeUsername(String chatroom, String username){
        if(roomUserList.containsKey(chatroom)) {
            List<String> rooms = roomUserList.get(chatroom);
            rooms.remove(username);
        } else {
            logger.error("No Chat Room with name exists..");
        }
    }
    
    /**
     * Method used to remove room from the store.
     * @param username
     * @param chatroom
     */
    public List<String> chatRoomsUsers(String chatroom){
        if(roomUserList.containsKey(chatroom)) {
            List<String> usernames = roomUserList.get(chatroom);
            return usernames;
        } else {
            return new ArrayList<String>();
        }
    }
}
