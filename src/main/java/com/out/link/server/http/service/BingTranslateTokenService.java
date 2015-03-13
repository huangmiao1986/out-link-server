package com.out.link.server.http.service;

import com.out.link.server.http.service.model.TranslateToken;

public interface BingTranslateTokenService {
	public TranslateToken getAccessToken() throws Exception;
}
