package com.out.link.server.http.cache;

import com.out.link.server.http.service.model.User;

public interface UserCache {
	
	public User getUserById(String id)throws Exception;
	
	public long addUser(User user)throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public void addUserMaxPoint(String id,long point)throws Exception;
	
	public long subUserMaxPoint(String id,long point)throws Exception;
	
}
