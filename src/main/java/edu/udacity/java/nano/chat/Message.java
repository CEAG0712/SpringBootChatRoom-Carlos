package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import static edu.udacity.java.nano.chat.MessageType.*;

//Gson use instead for JSON
//Jackson

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.



//    public static final String ENTER = "ENTER";
//    public static final String CHAT = "CHAT";
//    public static final String LEAVE = "LEAVE";

//    private String type;
    private Enum<MessageType>  type;
    private String username;
    private String msg;
    private int onlineCount;//

    public Message(Enum<MessageType> type, String username, String msg, int onlineCount) {
        this.type = type;
        this.username = username;
        this.msg = msg;
        this.onlineCount = onlineCount;
    }

    public static String jsonStr(MessageType type, String username, String msg, int onlineCount) {
        String json = new Gson().toJson(new Message(type, username, msg, onlineCount));

        return json;
       // return JSON.toJSONString(new Message(type, username, msg, onlineCount));
    }


    public Enum<MessageType> getType() {
        return type;
    }

    public void setType(Enum<MessageType> type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

}
