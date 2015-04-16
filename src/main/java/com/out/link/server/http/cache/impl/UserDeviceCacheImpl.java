package com.out.link.server.http.cache.impl;

import com.out.link.server.http.cache.BaseCache;
import com.out.link.server.http.cache.UserDeviceCache;

public class UserDeviceCacheImpl extends BaseCache implements UserDeviceCache{
	
	private static String OUT_LINK_DEVICE_USER_KEY = "OUT_LINK_DEVICE_USER_KEY:";
	
	@Override
	public String getUserIdByDeviceId(String deviceId) throws Exception{
		if(valadationRedisPassword()) {
			return redisServerCommon.get(OUT_LINK_DEVICE_USER_KEY+deviceId, 0);
		}
		return null;
	}

	@Override
	public void addDeviceIdAndUserId(String deviceId, String UserId) throws Exception{
		if(valadationRedisPassword()) {
			redisServerCommon.set(OUT_LINK_DEVICE_USER_KEY+deviceId, UserId, 0);
		}
	}

}
