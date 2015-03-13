package com.out.link.server.websocket.servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.out.link.server.http.cache.MessageCache;
import com.out.link.server.http.log.LoggerFactory;
import com.out.link.server.http.service.model.MessagePacketData;

public class WebSocketMessageInbound  extends MessageInbound {
	public Logger loggerError = LoggerFactory.getServerErrorLogger(WebSocketMessageInbound.class);
	
	Gson gson = new Gson();
	
	@Resource
	private MessageCache messageCache;
	
	//当前连接的用户名称  
    private final String user;  
  
    public WebSocketMessageInbound(String user) {  
        this.user = user;  
    }  
  
    public String getUser() {  
        return this.user;  
    }  
    
  //建立连接的触发的事件  
    @Override  
    protected void onOpen(WsOutbound outbound) {  
        //向连接池添加当前的连接对象  
        WebSocketMessageInboundPool.addMessageInbound(this);  
        try {
			Set<String> messages = messageCache.getMessageByReceiver(Long.parseLong(user));
			if(messages != null && messages.size() > 0) {
				for(String message : messages) {
					WebSocketMessageInboundPool.sendMessageToUser(user, message);
				}
			}
			messageCache.delMessageByUserId(Long.parseLong(user));
		} catch (NumberFormatException e) {
			loggerError.error("读取离线消息异常user:"+user, e);
		} catch (Exception e) {
			loggerError.error("读取离线消息异常user:"+user, e);
		}
    }  
  
    @Override  
    protected void onClose(int status) {  
        // 触发关闭事件，在连接池中移除连接  
        WebSocketMessageInboundPool.removeMessageInbound(this);  
    }  
    
	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		throw new UnsupportedOperationException("Binary message not supported."); 
	}

	@Override
	protected void onTextMessage(CharBuffer body) throws IOException {
		MessagePacketData message = gson.fromJson(body.toString(), MessagePacketData.class);
        WebSocketMessageInboundPool.sendMessageToUser(message.getReceiver()+"", body.toString());
	}

}
