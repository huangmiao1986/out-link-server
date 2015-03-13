package com.out.link.server.http.cache;

import com.out.link.server.http.service.model.TranslateToken;

public interface BingTranslateTokenCache  {
	
	public TranslateToken getTranslateToken() throws Exception;
	
	public void addTranslateToken(TranslateToken token) throws Exception;
}
