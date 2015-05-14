package com.out.link.server.http.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.out.link.server.http.log.LoggerFactory;
import com.out.link.server.http.redis.RedisOperator;
import com.out.link.server.http.service.UserService;
import com.out.link.server.http.service.model.User;

@Controller
public class UserActionController {
	
	public Logger loggerInfo = LoggerFactory.getServerInfoLogger(UserActionController.class);
	public Logger logger = LoggerFactory.getServerErrorLogger(UserActionController.class);
	
	private static String OUT_LINK_PASSWORD_WRONG_THREE_TIMES = "OUT_LINK_PASSWORD_WRONG_KEY:";
	
	@Resource
	private UserService userService;
	
	
	@Resource
	public RedisOperator redisServerCommon;
	
	Gson gson = new Gson();
	
	@RequestMapping(value = "action/user/loginUser", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String loginUser(@RequestParam(value="deviceId",required = true) String deviceId) {
		User user = null;
		try {
			if(StringUtils.isNotBlank(deviceId)) {
				user = userService.getUserByDeviceId(deviceId);
				if(user == null) {
					user = new User();
					long userId = userService.addUser(deviceId, user);
					user.setId(userId);
					user.setData_status((short)0);
				} else {
					user.setPassword("");
					if(StringUtils.isNotBlank(user.getAvatar_url()) 
							&& StringUtils.isNotBlank(user.getNick_name())
							&& StringUtils.isNotBlank(user.getCountry())
							&& StringUtils.isNotBlank(user.getPassword())
							) {
						user.setData_status((short)1);
					} else {
						user.setData_status((short)0);
					}
					return  "{ \"ret\" :0,\"user\":"+gson.toJson(user)+"}";
				}
			} else {
				return "{ \"ret\" : 1, \"err\" : \"deviceId is null \"}";
			}
		} catch (Exception e) {
			logger.error("login exception", e);
			return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0,\"user\":"+gson.toJson(user)+"}";
	}
	
	@RequestMapping(value = "action/user/uploadUserData", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String uploadUserData(@RequestBody String body) {
		User user = null;
		try {
			user  = gson.fromJson(body, User.class);
			if(user == null) {
				return "{ \"ret\" : 1, \"err\" : \"user is null \"}";
			} else {
				userService.updateUser(user);
			}
		} catch (Exception e) {
			logger.error("login exception", e);
			return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0,\"sign\":\"0\",\"user\":"+gson.toJson(user)+"}";//sgin 标识是注册还是登录
	}
	
	@RequestMapping(value = "action/user/transferPoint", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String transferPoint(
			@RequestParam(value="toUserId",required = true) String toUserId,
			@RequestParam(value="fromUserId",required = true) String fromUserId,
			@RequestParam(value="password",required = true) String password
			) {
		User user = null;
		try {
			if(!toUserId.equals(fromUserId)) {
				String times = redisServerCommon.get(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+fromUserId, 0);
				if(StringUtils.isNotBlank(times) && Integer.parseInt(times) == 3) {
					Long time = redisServerCommon.ttl(OUT_LINK_PASSWORD_WRONG_THREE_TIMES, 0)/60;
					return "{ \"ret\" : 1, \"err\" : \"Wrong password more than 3 times, please try again after" +time+ "minutes\"}";
				}
				boolean flag = userService.valadationPassword(fromUserId, password);
				if(flag) {
					user = userService.getUserById(fromUserId);
					if(user != null) {
						if(userService.subUserMaxPoint(fromUserId, user.getMax_point()) == 0)
							userService.addUserMaxPoint(fromUserId, user.getMax_point());
					} else {
						return "{ \"ret\" : 1, \"err\" : \"user is not exsits \"}";
					}
				} else {
					redisServerCommon.expire(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+fromUserId, 1800, 0);
					redisServerCommon.incr(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+fromUserId, 0);
					return "{ \"ret\" : 1, \"err\" : \"userId or password wrong \"}";
				}
			} else {
				return "{ \"ret\" : 1, \"err\" : \"The same user \"}";
			}
		} catch (Exception e) {
			 logger.error("transferPoint exception", e);
			 return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0,\"userPoint\":\""+user.getMax_point()+"\"}";
	}
	
	@RequestMapping(value = "action/user/updateUserGeo", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updateUserGeo(
			@RequestParam(value="userId",required = true) String userId,
			@RequestParam(value="lat",required = true) double lat,
			@RequestParam(value="lon",required = true) double lon,
			@RequestParam(value="country",required = true) String country
			) {
		try {
			userService.updateGeoByUserId(Long.parseLong(userId), country, lat, lon);
		} catch (Exception e) {
			logger.error("updateUserGeo exception", e);
			 return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0}";
	}
	
	@RequestMapping(value = "action/user/getNearbyUsers", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getNearbyUsers(
			@RequestParam(value="userId",required = true) String userId,
			@RequestParam(value="lat",required = true) double lat,
			@RequestParam(value="lon",required = true) double lon,
			@RequestParam(value="country",required = true) String country,
			@RequestParam(value="raidus",required = true) int raidus,
			@RequestParam(value="start",required = true) int start,
			@RequestParam(value="end",required = true) int end
			) {
		List<User> users = new ArrayList<User>();
		try {
			users = userService.getUsersByGeo(Long.parseLong(userId), country, lat, lon, raidus, start, end);
		} catch (Exception e) {
			logger.error("getNearbyUsers exception", e);
			 return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0,\"users\":"+gson.toJson(users)+"\"count\":"+users.size()+"}";
	}
	
	@RequestMapping(value = "action/user/resetPassword", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String resetPassword(
			@RequestParam(value="userId",required = true) String userId,
			@RequestParam(value="oldPassword",required = true) String oldPassword,
			@RequestParam(value="newPassword",required = true) String newPassword
			) {
		try {
			String times = redisServerCommon.get(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+userId, 0);
			if(StringUtils.isNotBlank(times) && Integer.parseInt(times) == 3) {
				Long time = redisServerCommon.ttl(OUT_LINK_PASSWORD_WRONG_THREE_TIMES, 0)/60;
				return "{ \"ret\" : 1, \"err\" : \"Wrong password more than 3 times, please try again after" +time+ "minutes\"}";
			}
			boolean result = userService.valadationPassword(userId, oldPassword);
			if(result) {
				userService.resetPassword(userId, newPassword);
			} else {
				redisServerCommon.expire(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+userId, 1800, 0);
				redisServerCommon.incr(OUT_LINK_PASSWORD_WRONG_THREE_TIMES+userId, 0);
				return "{ \"ret\" : 1, \"err\" : \"Old password input errors!\"}";
			}
		} catch (Exception e) {
			logger.error("getNearbyUsers exception", e);
			return "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" :0}";
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	 public String handleIOException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	 }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public String handleMissParaException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	}
}
