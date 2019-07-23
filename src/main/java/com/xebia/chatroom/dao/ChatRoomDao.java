package com.xebia.chatroom.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author heenanagpal
 *
 */
@Repository
public class ChatRoomDao {
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomDao.class);

    private static Map<String, String> roomCreatorList = new HashMap<String, String>();

    /**
     * Method used to add room for the user.
     * @param username
     * @param chatroom
     */
    public void addRoom(String username, String chatroom){
        if(!roomCreatorList.containsKey(chatroom)) {
            roomCreatorList.put(chatroom, username);
        } else {
            logger.error("room with same name already exists.");
        }
    }
    
    /**
     * Method used to remove user from the list
     * @param username
     */
    public void deleteRoom(String username, String chatroom){
        if(roomCreatorList.containsKey(chatroom) &&
                roomCreatorList.get(chatroom).equals(username)) {
            roomCreatorList.remove(chatroom);
        } else {
            logger.error("No room with name of user is not allowed to delete chtroom.");
        }
    }
    
    /**
     * Method used to return roomList.
     * @return
     */
    public Map<String, String> roomList(){
        return roomCreatorList;
    }
    
}
