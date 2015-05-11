package com.out.link.server.http.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.Gson;

public class HttpClientTest {
	public static void main(String[] args) {
//		int count = 5-1;
//		int rankNum = 3;
//		int totalCount = 10;
//		if(totalCount < count) {
//			count = totalCount;
//		}
//		int front = count / 2;
//		int follow = count - front;
//		System.out.println("front:"+front+" follow:"+follow);
//		
//		if(totalCount - rankNum < follow) {
//			front = front + (follow - (totalCount -rankNum));
//			follow = totalCount -rankNum;
//		}
//		if((rankNum -1) < front ) {
//			follow = follow + (front - (rankNum -1 ));
//			front = rankNum -1;
//		}
//		System.out.println("front:"+front+" follow:"+follow);
//		System.out.println("start:" + (rankNum - front) + "end:" + (rankNum + follow));
//		long time = 1588/60;
//		System.out.println(time);
		test();
	}
	@SuppressWarnings("deprecation")
	public static void test() {
		Gson gson = new Gson();
//		String json = gson.toJson(token);
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod("http://123.57.87.198:8080/out-link-server/action/translate/getTranslateNamesByLocale?locale=en");
		getMethod.addRequestHeader( "Content-Type","application/json" );
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("text", "my name is li");
//		map.put("password", "test123");
//		map.put("country", "zh-chs");
//		map.put("gender", "0");
//		map.put("device_id", "1234567890");
//		map.put("locale", "zh-chs");
//		PostMethod postMethod = new PostMethod("http://123.57.87.198:8080/out-link-server/action/user/loginUser");
//		postMethod.addRequestHeader( "Content-Type","application/json" );
//		postMethod.setRequestBody(gson.toJson(map));
//		postMethod.setRequestBody(json);
		try {
			int statusCode = client.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				String result = new String(getMethod.getResponseBody(), "UTF-8");
				System.out.println(result);
			} else {
				String result = new String(getMethod.getResponseBody(), "UTF-8");
				System.out.println(result);
			}
		} catch (Exception e) {
		} finally {
			getMethod.releaseConnection();
		}
	}
}
