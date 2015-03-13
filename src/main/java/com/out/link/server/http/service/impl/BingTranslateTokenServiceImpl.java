package com.out.link.server.http.service.impl;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.out.link.server.http.cache.BingTranslateTokenCache;
import com.out.link.server.http.service.BingTranslateTokenService;
import com.out.link.server.http.service.model.TranslateToken;

public class BingTranslateTokenServiceImpl implements BingTranslateTokenService {
	
	@Resource
	private BingTranslateTokenCache bingTranslateTokenCache;
	
	private static String TRANSLATE_TOKEN_URL = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
	
	Gson gson = new Gson();
	@Override
	public TranslateToken getAccessToken() throws Exception{
		TranslateToken token = bingTranslateTokenCache.getTranslateToken();
		if(token != null) {
			if(validationAccesToken(token.getAccess_token()) == 0) {
				token =  getNewAccessToken();
				bingTranslateTokenCache.addTranslateToken(token);
			}
		} else {
			token =  getNewAccessToken();
			bingTranslateTokenCache.addTranslateToken(token);
		}
		return token;
	}
	
	private TranslateToken getNewAccessToken() {
		TranslateToken token =  null;
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(TRANSLATE_TOKEN_URL);
		postMethod.addRequestHeader( "Content-Type","application/x-www-form-urlencoded" );
		postMethod.setParameter("grant_type", "client_credentials");
		postMethod.setParameter("client_id", "bingappid1986");
		postMethod.setParameter("client_secret", "xSZtEVPjl3uUPOlXlDzsytuSYEgTHQOo4FE0xQqDIK8=");
		postMethod.setParameter("scope", "http://api.microsofttranslator.com");
		try{
			int statusCode = client.executeMethod( postMethod );
			result = new String(postMethod.getResponseBody(),"UTF-8");
            if(statusCode != HttpStatus.SC_OK){
            	System.out.println("错误码："+statusCode+" result:"+result);
            } else { 
            	token = gson.fromJson(result, TranslateToken.class);
            	System.out.println("access_toke:"+token.getAccess_token()+" expires_in:"+token.getExpires_in());
            }
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	postMethod.releaseConnection();
        }
		return token;
	}
	
	private int validationAccesToken(String accessToken) {
		if(StringUtils.isNotBlank(accessToken)) {
			int start = accessToken.indexOf("ExpiresOn=");
			int end = accessToken.indexOf("&", start);
			long expiresTime = Long.parseLong(accessToken.substring(start+"ExpiresOn=".length(), end));
			if(expiresTime - System.currentTimeMillis()/1000 > 0)
				return 1;
			else 
				return 0;
		} else {
			return 0;
		}
	}
	
	public static void main(String[] args) {
		String ss = "i   ma  ll ";
		int count = 0;
		for(char s : ss.toCharArray()) {
			if(s != ' ')
				count = count + 1;
		}
		System.out.println(count);
}
}
