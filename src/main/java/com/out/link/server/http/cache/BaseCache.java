package com.out.link.server.http.cache;

import javax.annotation.Resource;

import com.out.link.server.http.redis.RedisOperator;

public class BaseCache {
	@Resource
	public RedisOperator redisServerCommon;
	
	public boolean valadationRedisPassword() throws Exception{
		if("OK".equals(redisServerCommon.auth("test123", 0))){
			return true;
		} {
			throw new Exception("redis password wrong");
		}
	}
}
