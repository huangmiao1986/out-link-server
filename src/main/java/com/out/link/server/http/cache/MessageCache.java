package com.out.link.server.http.cache;

import java.util.Set;

import com.out.link.server.http.service.model.MessagePacketData;

public interface MessageCache {
	/**
	 * 根据用户获取离线消息
	 * 
	 * @param receiver
	 * @return
	 * @throws Exception
	 */
	public  Set<String> getMessageByReceiver(long receiver) throws Exception;
	/**
	 * 增加离线消息
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void addMessage(MessagePacketData message) throws Exception;
	/**
	 * 删除离线消息
	 * 
	 * @param receiver
	 * @throws Exception
	 */
	public void delMessageByUserId(long receiver) throws Exception;
}
