package com.out.link.server.http.cache;

public interface UserDeviceCache {
	/**
	 * 根据设备号获取用户Id
	 * 
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public String getUserIdByDeviceId(String deviceId) throws Exception;
	/**
	 * 增加设备号与用户id关联
	 * 
	 * @param deviceId
	 * @param UserId
	 * @throws Exception
	 */
	public void addDeviceIdAndUserId(String deviceId,String UserId) throws Exception;
}
