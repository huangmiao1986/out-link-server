package com.out.link.server.websocket.servlet;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.out.link.server.http.cache.MessageCache;
import com.out.link.server.http.cache.UserCache;
import com.out.link.server.http.log.LoggerFactory;
import com.out.link.server.http.service.BingTranslateService;
import com.out.link.server.http.service.model.MessagePacketData;
import com.out.link.server.http.util.AppContextUtil;

public class WebSocketMessageInboundPool {
	public static Logger loggerError = LoggerFactory.getServerErrorLogger(WebSocketMessageInboundPool.class);
	
	//保存连接的MAP容器  
    private static final Map<String,WebSocketMessageInbound > connections = new HashMap<String,WebSocketMessageInbound>(); 
    
    private static MessageCache getMessageCache() {
		return (MessageCache) AppContextUtil.getBean("messageCache");
	}
    
    private static BingTranslateService getBingTranslateService() {
		return (BingTranslateService) AppContextUtil.getBean("bingTranslateService");
	}
    
    private static UserCache getUserCache() {
		return (UserCache) AppContextUtil.getBean("userCache");
	}
    
    private static Gson gson = new Gson();
      
    //向连接池中添加连接  
    public static void addMessageInbound(WebSocketMessageInbound inbound){  
        //添加连接  
        System.out.println("user : " + inbound.getUser() + " join..");  
        connections.put(inbound.getUser(), inbound);  
        try {
			getUserCache().addUserOnline(inbound.getUser());
		} catch (Exception e) {
			loggerError.error("user:"+inbound.getUser()+" add online exception:", e);
		}
    }  
      
    //获取所有的在线用户  
    public static Set<String> getOnlineUser(){  
        return connections.keySet();  
    }  
      
    public static void removeMessageInbound(WebSocketMessageInbound inbound){  
        //移除连接  
        System.out.println("user : " + inbound.getUser() + " exit..");  
        connections.remove(inbound.getUser());  
        try {
			getUserCache().removeUserOnline(inbound.getUser());
		} catch (Exception e) {
			loggerError.error("user:"+inbound.getUser()+" remove online exception:", e);
		}
    }  
      
    public static void sendMessageToUser(String user,String to,String message,boolean needTranslate){
    	StringBuffer newMessage = new StringBuffer(message);
    	if(needTranslate && StringUtils.isNotBlank(to)) {//翻译
	    	try {
				String translateText = getBingTranslateService().translateText(message, to);
				if(StringUtils.isNotBlank(translateText)) {
					newMessage.append("\n("+translateText+")");
				}
			} catch (Exception e1) {
				loggerError.error("user:"+user+" translate text:"+message+"to:"+to, e1);
			}
    	}
        try {  
            //向特定的用户发送数据  
            System.out.println("send message to user : " + user + " ,message content : " + newMessage.toString());  
            WebSocketMessageInbound inbound = connections.get(user);  
            if(inbound != null){  
                inbound.getWsOutbound().writeTextMessage(CharBuffer.wrap(newMessage.toString()));  
            } else {
            	try {
            		getMessageCache().addMessage(gson.fromJson(newMessage.toString(), MessagePacketData.class));
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
