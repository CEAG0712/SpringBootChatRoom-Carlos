package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {


    /**
     * All chat sessions.
     */
    private static final Logger logger = LogManager.getLogger(WebSocketChatServer.class);

    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
                logger.info("INFO: Sent message to All ");
            } catch (IOException e) {
                logger.error("ERROR MESSAGE " + e.getMessage());
            }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        session.getUserProperties().put("username", username); //Add user to session
        onlineSessions.put(session.getId(), session); //Add session
        sendMessageToAll(Message.jsonStr(Message.ENTER, getUserFromSession(session), "", onlineSessions.size()));
        logger.info("USER "+ getUserFromSession(session) +" is online and ready for chat");
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = JSON.parseObject(jsonStr, Message.class);
        String userInSession = getUserFromSession(session);
        if(userInSession(session, message)){
            sendMessageToAll(Message.jsonStr(Message.CHAT,userInSession , message.getMsg(), onlineSessions.size()));
            logger.info("USER " + userInSession + " Sent message successfully");
        } else {
            logger.error("ERROR " + message.getUsername()+" Not in session, cannot send messages" );
        }

    }


    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        String userLeaving = getUserFromSession(session);
        onlineSessions.remove(session.getId());
        sendMessageToAll(Message.jsonStr(Message.LEAVE, userLeaving, "", onlineSessions.size()));
        logger.info("USER" + userLeaving+" LOGGED OFF");
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //User logger instead
        //error.printStackTrace();
        logger.error(error.getMessage());
    }

    //Helper methods
    private String getUserFromSession(Session session){
        String user = session.getUserProperties().get("username").toString();
        return user;
    }


    private boolean userInSession(Session session, Message message){
        return session.getUserProperties().get("username").toString().equals(message.getUsername());
    }

}
