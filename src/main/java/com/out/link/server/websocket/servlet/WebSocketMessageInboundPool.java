package com.out.link.server.websocket.servlet;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.out.link.server.http.cache.MessageCache;
import com.out.link.server.http.log.LoggerFactory;
import com.out.link.server.http.service.model.MessagePacketData;

public class WebSocketMessageInboundPool {
	public static Logger loggerError = LoggerFactory.getServerErrorLogger(WebSocketMessageInboundPool.class);
	
	//保存连接的MAP容器  
    private static final Map<String,WebSocketMessageInbound > connections = new HashMap<String,WebSocketMessageInbound>(); 
    
    @Resource
	private static MessageCache messageCache;
    
    private static Gson gson = new Gson();
      
    //向连接池中添加连接  
    public static void addMessageInbound(WebSocketMessageInbound inbound){  
        //添加连接  
        System.out.println("user : " + inbound.getUser() + " join..");  
        connections.put(inbound.getUser(), inbound);  
    }  
      
    //获取所有的在线用户  
    public static Set<String> getOnlineUser(){  
        return connections.keySet();  
    }  
      
    public static void removeMessageInbound(WebSocketMessageInbound inbound){  
        //移除连接  
        System.out.println("user : " + inbound.getUser() + " exit..");  
        connections.remove(inbound.getUser());  
    }  
      
    public static void sendMessageToUser(String user,String message){  
        try {  
            //向特定的用户发送数据  
            System.out.println("send message to user : " + user + " ,message content : " + message);  
            WebSocketMessageInbound inbound = connections.get(user);  
            if(inbound != null){  
                inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
            } else {
            	try {
					messageCache.addMessage(gson.fromJson(message, MessagePacketData.class));
				} catch (JsonSyntaxException e) {
					loggerError.error("添加离线消息异常", e);
				} catch (Exception e) {
					loggerError.error("添加离线消息异常", e);
				}
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    //向所有的用户发送消息  
    public static void sendMessage(String message){  
        try {  
            Set<String> keySet = connections.keySet();  
            for (String key : keySet) {  
                WebSocketMessageInbound inbound = connections.get(key);  
                if(inbound != null){  
                    System.out.println("send message to user : " + key + " ,message content : " + message);  
                    inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
