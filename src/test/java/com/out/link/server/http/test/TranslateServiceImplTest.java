package com.out.link.server.http.test;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.out.link.server.http.service.BingTranslateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext*.xml" })
public class TranslateServiceImplTest {
	@Resource
	private BingTranslateService bingTranslateService;
	
	@Test
	public void test() throws Exception{
		Gson gson = new Gson();
//		String result = bingTranslateService.translateText("my    name    is  li",  "zh-chs");
//		List<String> languageCodes = bingTranslateService.getLanguagesForTranslate();
//		languageCodes.add("en");
//		languageCodes.add("zh");
//		languageCodes.add("de");
//		System.out.println(bingTranslateService.getLanguageNames("en", languageCodes));
		System.out.println(bingTranslateService.getLangTranslateAndName());
//		System.out.println(result);
		System.out.println(gson.toJson(bingTranslateService.getLangTranslateAndNameByLocale("zh-chs"), new TypeToken<Map<String,String>>(){}.getType()));
	}
}
