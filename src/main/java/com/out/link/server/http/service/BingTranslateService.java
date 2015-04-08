package com.out.link.server.http.service;

import java.util.List;
import java.util.Map;

public interface BingTranslateService {
	/**
	 * 翻译
	 * 
	 * @param translateText
	 * @param to 要翻译的目标语言
	 * @return
	 * @throws Exception
	 */
	public String translateText(String translateText,String to) throws Exception;
	
	/**
	 * 获取语言（code），以及语言名称
	 * 此处获取的语言名称是对应的语言书写
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getLangTranslateAndName() throws Exception;
	/**
	 * 获取语言（code），以及语言名称
	 * 此处获取的语言名称是指定语言书写
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getLangTranslateAndNameByLocale(String locale) throws Exception;
	/**
	 * 获取所有的语言（code）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getLanguagesForTranslate() throws Exception;
	/**
	 * 根据指定语言获取指定语言code的名称
	 * 
	 * @param locale 指定的语言书写
	 * @param languageCodes
	 * @return
	 * @throws Exception
	 */
	public List<String> getLanguageNames(String locale,List<String> languageCodes) throws Exception;
	/**
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLanguagesForSpeak() throws Exception;
}
