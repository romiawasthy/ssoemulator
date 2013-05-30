/**
 * 
 */
package com.romi.sample.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpHeadersRequestWrapper extends HttpServletRequestWrapper {
	private Map<String, String> customHeaders = new HashMap<String, String>();

	public HttpHeadersRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addCustomHeader(String name, String value){
		customHeaders.put(name, value);
	}
	/**
	 * 
	 */
	public String getHeader(String name){
		String header = null;
		if (customHeaders.containsKey(name)){
			header = (String) customHeaders.get(name);
		} else {
			header = super.getHeader(name);
		}
		
		return header;
	}
	
	

}
