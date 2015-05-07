package com.out.link.server.http.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BingTranslateLangCache;
import com.out.link.server.http.service.BingTranslateService;
import com.out.link.server.http.service.BingTranslateTokenService;
import com.out.link.server.http.service.model.TranslateToken;
public class BingTranslateServiceImpl implements BingTranslateService {
	
	private static String TRANSLATE_METHOD_URL = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text=%s&to=%s"; 
	private static String GET_LANG_NAMES = "http://api.microsofttranslator.com/v2/Http.svc/GetLanguageNames?locale=%s";
	private static String GET_LANG_FOR_SPEAK = "http://api.microsofttranslator.com/V2/Http.svc/GetLanguagesForSpeak";
	private static String GET_LANG_FOR_TRANSLATE = "http://api.microsofttranslator.com/V2/Http.svc/GetLanguagesForTranslate";
	@Resource
	private BingTranslateTokenService bingTranslateTokenService;
	
	@Resource
	private BingTranslateLangCache bingTranslateLangCache;
	
	Gson gson = new Gson();
	@Override
	public String translateText(String translateText,String to) throws Exception{
		TranslateToken authToken = bingTranslateTokenService.getAccessToken();
		String targetText = null;
		if(authToken != null) {
			GetMethod getMethod = null;
			try{
				HttpClient client = new HttpClient();
				String url = String.format(TRANSLATE_METHOD_URL, URLEncoder.encode(translateText,"UTF-8"),to);
				getMethod = new GetMethod(url);
				getMethod.setRequestHeader("Authorization", "Bearer " + authToken.getAccess_token());
				int statusCode = client.executeMethod( getMethod );
	            if(statusCode != HttpStatus.SC_OK){
	            	System.out.println("错误码："+statusCode);
	            } else { 
	            	String result = new String(getMethod.getResponseBody(),"UTF-8");
	            	if(StringUtils.isNotBlank(result)) {
	            		Document doc = DocumentHelper.parseText(result.trim());
	            		Element root = doc.getRootElement();	
	            		targetText = root.getText();
	            	}
	            }
	        }catch(Exception e){
	        }finally{
	        	if(getMethod != null)
	        		getMethod.releaseConnection();
	        }
		} else {
			System.out.println("获取bing，Token失败！");
		}
		return targetText;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLanguagesForTranslate() throws Exception{
		List<String> translateLangs = new ArrayList<String>();
		TranslateToken authToken = bingTranslateTokenService.getAccessToken();
		GetMethod getMethod = null;
		if(authToken != null) {
			try{
				HttpClient client = new HttpClient();
				getMethod = new GetMethod(GET_LANG_FOR_TRANSLATE);
				getMethod.setRequestHeader("Authorization", "Bearer " + authToken.getAccess_token());
				int statusCode = client.executeMethod( getMethod );
		        if(statusCode != HttpStatus.SC_OK){
		        	System.out.println("错误码："+statusCode);
	        } else { 
	        	String result = new String(getMethod.getResponseBody(),"UTF-8");
	        	if(StringUtils.isNotBlank(result)) {
	            	Document doc = DocumentHelper.parseText(result.trim());
            		Element root = doc.getRootElement();
            		for(Iterator<Element> it = root.elementIterator("string");it.hasNext();) {
            			translateLangs.add(it.next().getText());
            		}
	        	}
	        }
	    }catch(Exception e){
	    }finally{
	    	if(getMethod != null)
	    		getMethod.releaseConnection();
	    }
		} else {
			System.out.println("获取bing，Token失败！");
		}
		return translateLangs;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<String> getLanguageNames(String locale, List<String> languageCodes) throws Exception{
		List<String> names = new ArrayList<String>();
		String requestXml = createRequestXml(languageCodes);
		if(StringUtils.isNotBlank(requestXml)) {
			TranslateToken authToken = bingTranslateTokenService.getAccessToken();
			String result = null;
			if(authToken != null) {
				HttpClient client = new HttpClient();
				String url = String.format(GET_LANG_NAMES, locale);
				PostMethod postMethod = new PostMethod(url);
				postMethod.addRequestHeader( "Content-Type","text/xml" );
				postMethod.setRequestHeader("Authorization", "Bearer " + authToken.getAccess_token());
				postMethod.setRequestBody(requestXml);
				try{
					int statusCode = client.executeMethod( postMethod );
					result = new String(postMethod.getResponseBody(),"UTF-8");
		            if(statusCode != HttpStatus.SC_OK){
		            	System.out.println("错误码："+statusCode+" result:"+result);
		            } else { 
		            	
		            	Document doc = DocumentHelper.parseText(result.trim());
	            		Element root = doc.getRootElement();
	            		for(Iterator<Element> it = root.elementIterator("string");it.hasNext();) {
	            			names.add(it.next().getText());
	            		}
		            }
		        }catch(Exception e){
		        	e.printStackTrace();
		        }finally{
		        	postMethod.releaseConnection();
		        }
			} else {
				System.out.println("获取bing，Token失败！");
			}
		} else {
			System.out.println("languageCodes is null");
		}
		return names;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLanguagesForSpeak() throws Exception{
		List<String> speakLangs = new ArrayList<String>();
		TranslateToken authToken = bingTranslateTokenService.getAccessToken();
		GetMethod getMethod = null;
		if(authToken != null) {
			try{
				HttpClient client = new HttpClient();
				getMethod = new GetMethod(GET_LANG_FOR_SPEAK);
				getMethod.setRequestHeader("Authorization", "Bearer " + authToken.getAccess_token());
				int statusCode = client.executeMethod( getMethod );
		        if(statusCode != HttpStatus.SC_OK){
		        	System.out.println("错误码："+statusCode);
	        } else { 
	        	String result = new String(getMethod.getResponseBody(),"UTF-8");
	        	if(StringUtils.isNotBlank(result)) {
	            	Document doc = DocumentHelper.parseText(result.trim());
            		Element root = doc.getRootElement();
            		for(Iterator<Element> it = root.elementIterator("string");it.hasNext();) {
            			speakLangs.add(it.next().getText());
            		}
	        	}
	        }
	    }catch(Exception e){
	    }finally{
	    	if(getMethod != null)
	    		getMethod.releaseConnection();
	    }
		} else {
			System.out.println("获取bing，Token失败！");
		}
		return speakLangs;
	}
	
	private String createRequestXml(List<String> languageCodes) throws Exception{
		StringBuffer requestXml =  new StringBuffer("<ArrayOfstring xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">");
        if(languageCodes != null && languageCodes.size() > 0){
           for(String code : languageCodes)
        	   requestXml.append( "<string>"+code+"</string>");
        } else {
            return null;
        }
        requestXml.append( "</ArrayOfstring>");
        return requestXml.toString();
	}

	@Override
	public Map<String, String> getLangTranslateAndName() throws Exception{
		Map<String,String> map = bingTranslateLangCache.getTransLang();
		if(map != null && !map.isEmpty()) 
			return map;
		System.out.println("init translate language.....");
		List<String> translateKyes = getLanguagesForTranslate();
		List<String> trnaslateValues = new ArrayList<String>();
		if(trnaslateValues != null && translateKyes.size() > 0) {
			for(String key : translateKyes) {
				List<String> lang = new ArrayList<String>();
				lang.add(key);
				trnaslateValues.add(getLanguageNames(key, lang).get(0));
			}
		}
		if(trnaslateValues != null && trnaslateValues.size() > 0) {
			map = new HashMap<String, String>();
			for(int i =0 ;i <translateKyes.size() ;i++) {
				map.put(translateKyes.get(i), trnaslateValues.get(i));
			}
			bingTranslateLangCache.addTransLang(map);
		}
		return map;
	}

	@Override
	public Map<String, String> getLangTranslateAndNameByLocale(String locale) throws Exception{
		Map<String,String> map = bingTranslateLangCache.getTransLangByLocale(locale);
		if(map != null && !map.isEmpty()) 
			return map;
		System.out.println("init translate language by locale.....");
		List<String> translateKyes = getLanguagesForTranslate();
		List<String> trnaslateValues = getLanguageNames(locale, translateKyes);
		if(trnaslateValues != null && trnaslateValues.size() > 0) {
			map = new HashMap<String, String>();
			for(int i =0 ;i <translateKyes.size() ;i++) {
				map.put(translateKyes.get(i), trnaslateValues.get(i));
			}
			bingTranslateLangCache.addTransLangByLocale(locale, map);
		}
		return map;
	}

	@Override
	public boolean checkSpeakLang(String lang) throws Exception {
		Map<String,String> map = bingTranslateLangCache.getSpeakLang();
		if(map == null || map.isEmpty()) {
			List<String> speakLangKeys = getLanguagesForSpeak();//code
			if(speakLangKeys != null && speakLangKeys.size() > 0) {
				Map<String,String> speakLangMap = new HashMap<String,String>();
				for(String key : speakLangKeys) {
					speakLangMap.put(key, "1");
				}
				bingTranslateLangCache.addSpeakLang(speakLangMap);
			}
		}
		return bingTranslateLangCache.checkSpeakLang(lang);
	}
}
