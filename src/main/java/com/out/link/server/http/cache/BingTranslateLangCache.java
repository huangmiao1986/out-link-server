package com.out.link.server.http.cache;

import java.util.Map;

public interface BingTranslateLangCache {
	/**
	 * 根据环境语言获取翻译语言map
	 * 
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getTransLangByLocale(String locale) throws Exception;
	/**
	 *增加环境语言对应的翻译语言map
	 * 
	 * @param locale
	 * @param transLangMap
	 * @throws Exception
	 */
	public void addTransLangByLocale(String locale,Map<String,String> transLangMap) throws Exception;
	/**
	 * 获取默认翻译语言map
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getTransLang() throws Exception;
	/**
	 * 增加翻译语言
	 * 
	 * @param transLangMap
	 * @throws Exception
	 */
	public void addTransLang(Map<String,String> transLangMap) throws Exception;
	/**
	 * 增加语音语言
	 * 
	 * @param speakLangMap
	 * @throws Exception
	 */
	public void addSpeakLang(Map<String,String> speakLangMap) throws Exception; 
	/**
	 * 检查语言在语音map中是否存在，以便决定能否语音读取
	 * 
	 * @param lang
	 * @throws Exception
	 */
	public boolean checkSpeakLang(String lang) throws Exception;
	/**
	 * 获取语音语言
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getSpeakLang() throws Exception;
}
