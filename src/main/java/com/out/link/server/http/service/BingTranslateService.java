package com.out.link.server.http.service;

import java.util.List;
import java.util.Map;

public interface BingTranslateService {
	
	public String translateText(String translateText,String to) throws Exception;
	
	public Map<String,String> getLangTranslateAndName() throws Exception;
	
	public Map<String,String> getLangTranslateAndNameByLocale(String locale) throws Exception;
	
	public List<String> getLanguagesForTranslate() throws Exception;
	
	public List<String> getLanguageNames(String locale,List<String> languageCodes) throws Exception;
	
	public String getLanguagesForSpeak() throws Exception;
}
