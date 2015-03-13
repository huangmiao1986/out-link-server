package com.out.link.server.http.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.out.link.server.http.log.LoggerFactory;
import com.out.link.server.http.service.BingTranslateService;
import com.out.link.server.http.service.UserService;
import com.out.link.server.http.service.model.TranslateRequest;

@Controller
public class BingTranslateController {
	public Logger loggerError = LoggerFactory.getServerErrorLogger(BingTranslateController.class);
	
	@Resource
	private BingTranslateService bingTranslateService;
	
	@Resource
	private UserService userService;
	
	Gson gson = new Gson();
	
	@RequestMapping(value = "translate/action/translateText", method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String translateText(
			@RequestBody String body
			) {
		String targetText = null;
		try {
			TranslateRequest request = gson.fromJson(body, TranslateRequest.class);
			if(request != null 
					&& StringUtils.isNotBlank(request.getText()) 
					&& StringUtils.isNotBlank(request.getTo()) 
					&& StringUtils.isNotBlank(request.getUid())) {
				int subPoint = countText(request.getText());
				long rePoint = userService.subUserMaxPoint(request.getUid(), subPoint);
				if(rePoint == 0) {
					targetText = bingTranslateService.translateText(request.getText(), request.getTo());
				} else {
					return "{ \"ret\" : 1, \"err\" : \"The rest of the points is not enough!\"}";
				}
			} else {
				return "{ \"ret\" : 1, \"err\" : \"uid or text or to is null!\"}";
			}
		} catch (Exception e) {
			loggerError.error("translate text exception", e);
			return  "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
		return  "{ \"ret\" : 0,\"target_text\":\""+targetText+"\"}";
	}
	
	@RequestMapping(value = "translate/action/getTranslateNames", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getTranslateNames(
			) {
		try {
			Map<String,String> map = bingTranslateService.getLangTranslateAndName();
			return gson.toJson(map, new TypeToken<Map<String,String>>(){}.getType());
		} catch (Exception e) {
			loggerError.error("getTranslateNames exception", e);
			return  "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
	}
	
	@RequestMapping(value = "translate/action/getTranslateNamesByLocale", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getTranslateNamesByLocale(
			@RequestParam String locale
			) {
		try {
			Map<String,String> map = bingTranslateService.getLangTranslateAndNameByLocale(locale);
			return gson.toJson(map, new TypeToken<Map<String,String>>(){}.getType());
		} catch (Exception e) {
			loggerError.error("getTranslateNamesByLocale exception", e);
			return  "{ \"ret\" : 1, \"err\" : \"" + e.getMessage() + "\"}";
		}
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	 public String handleIOException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	 }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public String handleMissParaException(Exception ex) {
		return "{ \"ret\" : 1, \"err\" : \"" + ex.getMessage() + "\"}" ;
	}
	//计算字符的个数，去掉空格数
	private int countText(String text) {
		int count = 0;
		for(char s : text.toCharArray()) {
			if(s != ' ')
				count = count + 1;
		}
		return count;
	}
}
