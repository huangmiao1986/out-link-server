package com.out.link.server.http.cache;

import com.out.link.server.http.service.model.User;

public interface UserCache {
	/**
	 * 根据用户Id获取用户信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public User getUserById(String id)throws Exception;
	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public long addUser(User user)throws Exception;
	/**
	 * 更新用户
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateUser(User user) throws Exception;
	/**
	 * 增加翻译字符点数
	 * 
	 * @param id
	 * @param point
	 * @throws Exception
	 */
	public void addUserMaxPoint(String id,long point)throws Exception;
	/**
	 * 减去对对应的翻译点数
	 * 
	 * @param id
	 * @param point
	 * @return
	 * @throws Exception
	 */
	public long subUserMaxPoint(String id,long point)throws Exception;
	/**
	 * 增加用户在线记录
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void addUserOnline(String id) throws Exception;
	/**
	 * 获取用户是否在线
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean getUserOnline(String id) throws Exception;
	/**
	 * 从在线中移除
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public long removeUserOnline(String id) throws Exception;
	
	/**
	 * 密码重置
	 * 
	 * @param id
	 * @param password
	 * @throws Exception
	 */
	public void resetPassword(String id,String password) throws Exception;
}
