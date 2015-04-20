package com.out.link.server.http.cache.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BaseCache;
import com.out.link.server.http.cache.UserCache;
import com.out.link.server.http.service.model.User;
public class UserCacheImpl extends BaseCache implements UserCache{
	Gson gson = new Gson();
	
	private static String OUT_LINK_USER_KEY = "OUT_LINK_USER_KEY:";
	
	private static String OUT_LINK_USER_ID_KEY = "OUT_LINK_USER_ID_KEY:";
	
	private static String OUT_LINK_USER_ONLINE_KEY = "OUT_LINK_USER_ONLINE_KEY";
	
	private SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	
	@Override
	public User getUserById(String id) throws Exception {
		if(valadationRedisPassword()) {
			User user = null;
			Map<String,String> userMap = redisServerCommon.hgetAll(OUT_LINK_USER_KEY+id, 0);
			if(userMap != null && !userMap.isEmpty()) {
				user = new User();
				convertMapToUser(userMap,user);
			}
			return user;
		}
		return null;
	}

	@Override
	public long addUser(User user) throws Exception {
		if(valadationRedisPassword()) {
			long userId = 10000;
			if(user != null) {
				String userIdStr = redisServerCommon.get(OUT_LINK_USER_ID_KEY, 0);
				if(StringUtils.isBlank(userIdStr)) {
					redisServerCommon.set(OUT_LINK_USER_ID_KEY, userId+"", 0);
				} else {
					userId = Long.parseLong(userIdStr);
				}
				redisServerCommon.incr(OUT_LINK_USER_ID_KEY, 0);
				user.setId(userId);
				Map<String,String> userMap = new HashMap<String, String>();
				convertUserToMap(user,userMap);
				redisServerCommon.hmset(OUT_LINK_USER_KEY+userId, 0, userMap);
			}
			return userId;
		}
		return 0;
	}

	@Override
	public void addUserMaxPoint(String id, long point) throws Exception{
		if(valadationRedisPassword()) {
			Map<String,String> userMap = redisServerCommon.hgetAll(OUT_LINK_USER_KEY+id, 0);
			long pointAdd = Long.parseLong(userMap.get("max_point")) + point;
			redisServerCommon.hset(OUT_LINK_USER_KEY+id, "max_point", pointAdd+"", 0);
		}
	}

	private void convertUserToMap(User user,Map<String,String> userMap) throws ParseException {
		userMap.put("password", user.getPassword());
		userMap.put("avatar_url", user.getAvatar_url());
		userMap.put("create_time", sdf.format(user.getCreate_time()));
		userMap.put("update_time", sdf.format(user.getUpdate_time()));
		userMap.put("id", user.getId()+"");
		userMap.put("gender", user.getGender()+"");
		userMap.put("country", user.getCountry());
		userMap.put("max_point", user.getMax_point()+"");
		userMap.put("used_point", user.getUsed_point()+"");
		userMap.put("nick_name", user.getNick_name());
	}
	
	private void convertMapToUser(Map<String,String> map,User user) throws ParseException {
		if(StringUtils.isNotBlank(map.get("avatar_url")))
			user.setAvatar_url(map.get("avatar_url"));
		if(StringUtils.isNotBlank(map.get("create_time")))
			user.setCreate_time(sdf.parse("create_time"));
		if(StringUtils.isNotBlank(map.get("update_time")))
			user.setUpdate_time(sdf.parse("update_time"));
		if(StringUtils.isNotBlank(map.get("id")))
			user.setId(Long.parseLong(map.get("id")));
		if(StringUtils.isNotBlank(map.get("nick_name")))
			user.setNick_name(map.get("nick_name"));
		if(StringUtils.isNotBlank(map.get("gender")))
			user.setGender(Short.parseShort(map.get("gender")));
		if(StringUtils.isNotBlank(map.get("country")))
			user.setCountry(map.get("country"));
		if(StringUtils.isNotBlank(map.get("max_point")))
			user.setMax_point(Long.parseLong(map.get("max_point")));
		if(StringUtils.isNotBlank(map.get("used_point")))
			user.setUsed_point(Long.parseLong("used_point"));
	}

	@Override
	public long subUserMaxPoint(String id, long point) throws Exception {
		if(valadationRedisPassword()) {
			Map<String,String> userMap = redisServerCommon.hgetAll(OUT_LINK_USER_KEY+id, 0);
			long pointSub = Long.parseLong(userMap.get("max_point")) - point;
			if(pointSub < 0) {
				return Long.parseLong(userMap.get("max_point"));
			} else {
				redisServerCommon.hset(OUT_LINK_USER_KEY+id, "max_point", pointSub+"", 0);
				return 0;
			}
		}
		return 1;
	}

	@Override
	public void updateUser(User user) throws Exception {
		if(valadationRedisPassword() && user != null) {
			Map<String,String> userMap = new HashMap<String,String>();
			if(StringUtils.isNotBlank(user.getAvatar_url()))
				userMap.put("avatar_url", user.getAvatar_url());
			userMap.put("update_time", sdf.format(user.getUpdate_time()));
			userMap.put("gender", user.getGender()+"");
			if(StringUtils.isNotBlank(user.getCountry()))
				userMap.put("country", user.getCountry());
			if(StringUtils.isNotBlank(user.getNick_name()))
				userMap.put("nick_name", user.getNick_name());
			redisServerCommon.hmset(OUT_LINK_USER_KEY+user.getId(), 0, userMap);
		}
	}

	@Override
	public void addUserOnline(String id) throws Exception {
		if(valadationRedisPassword()) {
			redisServerCommon.zadd(OUT_LINK_USER_ONLINE_KEY, System.currentTimeMillis(), id, 0);
		}
	}

	@Override
	public boolean getUserOnline(String id) throws Exception {
		if(valadationRedisPassword()) {
			return redisServerCommon.sismember(OUT_LINK_USER_ONLINE_KEY, id, 0);
		}
		return false;
	}

	@Override
	public long removeUserOnline(String id) throws Exception {
		if(valadationRedisPassword()) {
			return redisServerCommon.zrem(OUT_LINK_USER_ONLINE_KEY, id, 0);
		}
		return 0;
	}

	@Override
	public void resetPassword(String id, String password) throws Exception {
		if(valadationRedisPassword()) {
			redisServerCommon.hset(OUT_LINK_USER_KEY+id, "password", password, 0);
		}
	}
	
}
