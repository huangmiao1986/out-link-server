package com.out.link.server.http.cache;

import java.util.Map;

public interface BingTranslateLangCache {
	public Map<String,String> getTransLangByLocale(String locale) throws Exception;
	public void addTransLangByLocale(String locale,Map<String,String> transLangMap) throws Exception;
	public Map<String,String> getTransLang() throws Exception;
	public void addTransLang(Map<String,String> transLangMap) throws Exception;
}
