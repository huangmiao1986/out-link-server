package com.out.link.server.http.cache.impl;


import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BaseCache;
import com.out.link.server.http.cache.BingTranslateTokenCache;
import com.out.link.server.http.service.model.TranslateToken;
public class BingTranslateTokenCacheImpl extends BaseCache implements BingTranslateTokenCache {
	
	Gson gson = new Gson();
	
	private static String BING_ACCESS_TOKEN_KEY = "BING_ACCESS_TOKEN";
	
	@Override
	public TranslateToken getTranslateToken() throws Exception{
		if(valadationRedisPassword()) {
			String json = redisServerCommon.get(BING_ACCESS_TOKEN_KEY, 0);
			if(StringUtils.isNotBlank(json)) {
				return gson.fromJson(json, TranslateToken.class);
			}
		}
		return null;
	}
	@Override
	public void addTranslateToken(TranslateToken token) throws Exception {
		if(token != null && valadationRedisPassword())
			redisServerCommon.set(BING_ACCESS_TOKEN_KEY, gson.toJson(token), 0);
	}
}
