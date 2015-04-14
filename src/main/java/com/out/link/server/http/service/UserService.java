package com.out.link.server.http.service;

import java.util.List;

import com.out.link.server.http.service.model.User;

public interface UserService {
	/**
	 * 根据用户Id更新用户地理位置
	 * 
	 * @param userId
	 * @param country
	 * @param lat
	 * @param lon
	 * @return
	 * @throws Exception
	 */
	public int updateGeoByUserId(long userId,String country,double lat,double lon) throws Exception;
	/**
	 * 获取附近
	 * 
	 * @param userId
	 * @param country
	 * @param lat
	 * @param lon
	 * @param raidus
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsersByGeo(long userId,String country,double lat,double lon,int raidus,int start ,int end) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public User getUserById(String id)throws Exception;
	
	public User getUserByDeviceId(String deviceId)throws Exception;
	
	public long addUser(String deviceId,User user)throws Exception;
	
	public void addUserMaxPoint(String id,long point)throws Exception;
	
	public long subUserMaxPoint(String id,long point)throws Exception;
	
	public boolean valadationPassword(String userId,String password) throws Exception;
}
