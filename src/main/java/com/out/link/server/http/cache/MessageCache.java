package com.out.link.server.http.cache;

import java.util.Set;

import com.out.link.server.http.service.model.MessagePacketData;

public interface MessageCache {
	public  Set<String> getMessageByReceiver(long receiver) throws Exception;
	public void addMessage(MessagePacketData message) throws Exception;
	public void delMessageByUserId(long receiver) throws Exception;
}
