package com.out.link.server.http.service;

import java.util.List;

import com.out.link.server.http.service.model.User;

public interface UserService {
	
	public int updateGeoByUserId(long userId,String country,double lat,double lon) throws Exception;
	
	public List<User> getUsersByGeo(long userId,String country,double lat,double lon,int raidus,int start ,int end) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public User getUserById(String id)throws Exception;
	
	public User getUserByDeviceId(String deviceId)throws Exception;
	
	public long addUser(String deviceId,User user)throws Exception;
	
	public void addUserMaxPoint(String id,long point)throws Exception;
	
	public long subUserMaxPoint(String id,long point)throws Exception;
	
	public boolean valadationPassword(String userId,String password) throws Exception;
}
