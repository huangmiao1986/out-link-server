package com.out.link.server.http.cache;

public interface UserDeviceCache {
	public String getUserIdByDeviceId(String deviceId) throws Exception;
	public void addDeviceIdAndUserId(String deviceId,String UserId) throws Exception;
}
