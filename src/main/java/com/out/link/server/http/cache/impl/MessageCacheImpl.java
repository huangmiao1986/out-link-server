package com.out.link.server.http.cache.impl;

import java.util.Set;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BaseCache;
import com.out.link.server.http.cache.MessageCache;
import com.out.link.server.http.service.model.MessagePacketData;

public class MessageCacheImpl extends BaseCache implements MessageCache {

	Gson gson = new Gson();

	private static String OFFLINE_MESSAGE_DATA_KEY = "OFFLINE_MESSAGE_USER_KEY:";

	@Override
	public Set<String> getMessageByReceiver(long receiver) throws Exception {
		if (valadationRedisPassword()) {
			return redisServerCommon.zrange(OFFLINE_MESSAGE_DATA_KEY+receiver, 0, -1, 0);
		}
		return null;
	}

	@Override
	public void addMessage(MessagePacketData message) throws Exception {
		if (valadationRedisPassword()) {
			redisServerCommon.zadd(OFFLINE_MESSAGE_DATA_KEY+message.getReceiver(), System.currentTimeMillis(), gson.toJson(message), 0);
		}
	}

	@Override
	public void delMessageByUserId(long receiver) throws Exception {
		if(valadationRedisPassword()) {
			redisServerCommon.del(OFFLINE_MESSAGE_DATA_KEY+receiver, 0);
		}
	}

}
