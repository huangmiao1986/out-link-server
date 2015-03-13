package com.out.link.server.http.dao;

import java.util.List;

import com.out.link.server.http.service.model.UserGeo;

public interface UserGeoDAO {
	public List<Long> getUserIdsForGeo(long userId,String country,double lat,double lon,int raidus,int start ,int end);
	public UserGeo getUserGeoByUserId(long userId);
	public int updateGeoByUserId(long userId,double lat,double lon);
	public int addUserGeo(UserGeo userGeo);
}
