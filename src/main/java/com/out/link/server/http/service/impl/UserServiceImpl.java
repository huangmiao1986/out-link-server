package com.out.link.server.http.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.out.link.server.http.cache.UserCache;
import com.out.link.server.http.cache.UserDeviceCache;
import com.out.link.server.http.dao.UserGeoDAO;
import com.out.link.server.http.service.UserService;
import com.out.link.server.http.service.model.User;
import com.out.link.server.http.service.model.UserGeo;

public class UserServiceImpl implements UserService {

	@Resource
	private UserCache userCache;
	
	@Resource
	private UserDeviceCache userDeviceCache;
	
	@Resource
	private UserGeoDAO userGeoDAO;
	
	@Override
	public User getUserById(String id) throws Exception {
		return userCache.getUserById(id);
	}

	@Override
	public long addUser(String deviceId,User user) throws Exception {
		user.setMax_point(5000);
		long userId = userCache.addUser(user);
		userDeviceCache.addDeviceIdAndUserId(deviceId,  userId+"");
		return userId;
	}

	@Override
	public void addUserMaxPoint(String id, long point) throws Exception {
		userCache.addUserMaxPoint(id, point);
	}

	@Override
	public long subUserMaxPoint(String id, long point) throws Exception {
		return userCache.subUserMaxPoint(id, point);
	}

	@Override
	public User getUserByDeviceId(String deviceId) throws Exception {
		String userId = userDeviceCache.getUserIdByDeviceId(deviceId);
		return userCache.getUserById(userId);
	}

	@Override
	public boolean valadationPassword(String userId, String password) throws Exception{
		User user = userCache.getUserById(userId);
		if(user != null) {
			if(password.equals(user.getPassword())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateUser(User user) throws Exception {
		userCache.updateUser(user);
	}

	@Override
	public List<User> getUsersByGeo(long userId, String country, double lat,
			double lon, int raidus, int start, int end) throws Exception {
		List<Long> userIds = userGeoDAO.getUserIdsForGeo(userId, country, lat, lon, raidus, start, end);
		List<User> users = new ArrayList<User>();
		if(userIds != null && userIds.size() > 0) {
			for(long targetUserId:userIds) {
				User user = userCache.getUserById(targetUserId+"");
				if(userCache.getUserOnline(targetUserId+"")) {
					user.setOnline(1);
				}
				user.setPassword("");
				if(user != null)
					users.add(user);
			}
			return users;
		}
		return null;
	}

	@Override
	public int updateGeoByUserId(long userId, String country,double lat, double lon)
			throws Exception {
		if(userGeoDAO.getUserGeoByUserId(userId) != null) {
			return userGeoDAO.updateGeoByUserId(userId, lat, lon);
		} else {
			UserGeo userGeo = new UserGeo();
			userGeo.setCountry(country);
			userGeo.setLatitude(lat);
			userGeo.setLongitude(lon);
			userGeo.setUserId(userId);
			return userGeoDAO.addUserGeo(userGeo);
		}
	}

}
