package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
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
@ServerEndpoint("/chat")
public class WebSocketChatServer {


    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
               // System.out.println("Message:" + msg); -> user logger instead logger.WARN, INFO, ERROR, DEBUG
            } catch (IOException e) {
                //logger to log the exception Apache Log4j instead of printing
                e.printStackTrace();
            }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        onlineSessions.put(session.getId(), session);
        //add user to session - Mostafa to guide me
        sendMessageToAll(Message.jsonStr(Message.ENTER, "", "", onlineSessions.size()));
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        //Session parameter - Get the user out of the session
        //validate if the user sending the messages is in session or not - Generic function private function
        Message message = JSON.parseObject(jsonStr, Message.class);
        //user should be the user in session
        sendMessageToAll(Message.jsonStr(Message.CHAT, message.getUsername(), message.getMsg(), onlineSessions.size()));
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //validate if the user sending the messages is in session or not - Generic function private function
        onlineSessions.remove(session.getId());
        sendMessageToAll(Message.jsonStr(Message.LEAVE, "", "", onlineSessions.size()));
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //Mostafa to revisit later
        //User logger instead
        error.printStackTrace();
    }

}
