package com.out.link.server.http.dao;

import java.util.List;

import com.out.link.server.http.service.model.UserGeo;

public interface UserGeoDAO {
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
	 */
	public List<Long> getUserIdsForGeo(long userId,String country,double lat,double lon,int raidus,int start ,int end);
	/**
	 * 获取用户地理位置
	 * 
	 * @param userId
	 * @return
	 */
	public UserGeo getUserGeoByUserId(long userId);
	/**
	 * 更新用户地理位置
	 * 
	 * @param userId
	 * @param lat
	 * @param lon
	 * @return
	 */
	public int updateGeoByUserId(long userId,double lat,double lon);
	/**
	 * 增加用户地理位置
	 * 
	 * @param userGeo
	 * @return
	 */
	public int addUserGeo(UserGeo userGeo);
}
