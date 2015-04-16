package com.out.link.server.http.cache.impl;

import java.util.Map;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BaseCache;
import com.out.link.server.http.cache.BingTranslateLangCache;
public class BingTranslateLangCacheImpl extends BaseCache implements BingTranslateLangCache {
	
	Gson gson = new Gson();
	
	private static String BING_TRANS_LANG_KEY = "BING_TRANS_LANG";
	
	private static String BING_TRANS_LANG_LOCALE_KEY = "BING_TRANS_LANG:";
	
	@Override
	public Map<String,String> getTransLang() throws Exception{
		if(valadationRedisPassword()) {
			return redisServerCommon.hgetAll(BING_TRANS_LANG_KEY, 0);
		}
		return null;
	}

	@Override
	public void addTransLang(Map<String, String> transLangMap) throws Exception{
		if(valadationRedisPassword()) {
			redisServerCommon.hmset(BING_TRANS_LANG_KEY, 0, transLangMap);
		}
	}

	@Override
	public Map<String, String> getTransLangByLocale(String locale) throws Exception{
		if(valadationRedisPassword()) {
			return redisServerCommon.hgetAll(BING_TRANS_LANG_LOCALE_KEY+locale, 0);
		}
		return null;
	}

	@Override
	public void addTransLangByLocale(String locale,Map<String, String> transLangMap) throws Exception{
		if(valadationRedisPassword()) {
			redisServerCommon.hmset(BING_TRANS_LANG_LOCALE_KEY+locale, 0, transLangMap);
		}
	}
}
