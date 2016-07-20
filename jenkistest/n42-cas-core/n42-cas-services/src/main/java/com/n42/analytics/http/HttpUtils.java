package com.n42.analytics.http;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.n42.analytics.kairosclient.url.Kairosdb;

public class HttpUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String postRequest(String inputBody) {
		String kairosDBUrl = Kairosdb.HOST;
		kairosDBUrl = "http://"+Kairosdb.HOST+":"+Kairosdb.PORT+"/api/v1/datapoints/query?=";
		if (kairosDBUrl == null || inputBody == null) {
			LOG.warn("Either KairosDBUrl or InputBody is Null");
			return null;
		}
		
		String responseBody = null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(kairosDBUrl);
		
		RequestEntity requestEntity = null;
		
		try {
			requestEntity = new StringRequestEntity(inputBody, "text/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			responseBody = IOUtils.toString(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet());
		} catch (IOException e) {
			LOG.error("Unexpected exception : {}", e);
		} finally{
			postMethod.releaseConnection();
		}
		return responseBody;

	}
	
	public static String postUrlRequest(String url,String inputBody) {		
		String responseBody = null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);		
		RequestEntity requestEntity = null;		
		try {
			requestEntity = new StringRequestEntity(inputBody, "text/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			responseBody = IOUtils.toString(postMethod.getResponseBodyAsStream(), postMethod.getResponseCharSet());
		} catch (IOException e) {
			LOG.error("Unexpected exception : {}", e);
		} finally{
			postMethod.releaseConnection();
		}
		return responseBody;
	}
	
	public static String getUrlRequest(String url) {		
		String responseBody = null;
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);		
		try {
			client.executeMethod(getMethod);
			responseBody = IOUtils.toString(getMethod.getResponseBodyAsStream(), getMethod.getResponseCharSet());
		} catch (IOException e) {
			LOG.error("Unexpected exception : {}", e);
		} finally{
			getMethod.releaseConnection();
		}
		return responseBody;
	}
}
