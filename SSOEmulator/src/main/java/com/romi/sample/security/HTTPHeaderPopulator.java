/**
 * 
 */
package com.romi.sample.security;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;



/**
 * @author rawasthy
 * 
 */
@Component
public class HTTPHeaderPopulator
{
	/**
	 * Will populate HTTP Headers from the list of GrantedAuthorities created by emulated AuthenticationProvider
	 */
	public HttpServletRequest addHeaders(HttpServletRequest request, HttpServletResponse response)
	{
		
		AbstractAuthenticationToken principal = (AbstractAuthenticationToken) request.getUserPrincipal();
		request = new HttpHeadersRequestWrapper(request);
		String sUserID = principal.getName();

		
		((HttpHeadersRequestWrapper) request).addCustomHeader("iv-user", sUserID);
		((HttpHeadersRequestWrapper) request).addCustomHeader("authorities", getGroupsString(principal.getAuthorities()));
		
				
		return request;
	}

	private String getGroupsString(Collection<GrantedAuthority> authorities)
	{
		StringBuffer strAuthorities = new StringBuffer();
		for (GrantedAuthority authro : authorities)
		{
			strAuthorities.append(authro.getAuthority());
		}
		return strAuthorities.toString();
	}

	
}
