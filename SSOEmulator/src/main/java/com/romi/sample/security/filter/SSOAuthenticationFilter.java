/**
 * 
 */
package com.romi.sample.security.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;



/**
 * @author rawasthy
 * 
 */

public class SSOAuthenticationFilter extends GenericFilterBean {
	
	private static final Logger logger = LoggerFactory.getLogger(SSOAuthenticationFilter.class);
	private AuthenticationManager authenticationManager = null;
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	
	/**
	 * @param authenticationManager
	 *            The AuthenticationManager to use
	 */
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (logger.isDebugEnabled()) {
			logger.debug("Checking secure context token: "
					+ SecurityContextHolder.getContext().getAuthentication());
		}
		String sUserID = (String) ((HttpServletRequest) request)
				.getHeader("iv-user");

		if (requiresAuthentication(sUserID)) {
			doAuthenticate((HttpServletRequest) request, sUserID,
					(HttpServletResponse) response);
			
		}
		
		
		chain.doFilter(request, response);
	}

	private boolean requiresAuthentication(String principal) {
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication();

		if (currentUser == null) {
			return true;
		}

		if (!currentUser.getName().equals(principal)) {
			logger.debug("Pre-authenticated principal has changed to "
					+ principal + " and will be reauthenticated");

			return true;
		}

		return false;
	}

	/**
	 * Do the actual authentication for a pre-authenticated user.
	 */
	private void doAuthenticate(HttpServletRequest request, String principal,
			HttpServletResponse response) {
		Authentication authResult = null;

		if (principal == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No pre-authenticated principal found in request");
			}

			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("preAuthenticatedPrincipal = " + principal
					+ ", trying to authenticate");
		}

		try {
			principal = URLDecoder.decode(principal, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("Error URL decoding userid" + principal);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("WebSEAL iv_header present:" + principal
					+ ". Validating WebSEAL request");
		}
		

		try {
			PreAuthenticatedAuthenticationToken authRequest = new PreAuthenticatedAuthenticationToken(
					principal, "N/A",
					getGrantedAuthorities((String) request.getHeader("authorities")));
			authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
			authResult = authenticationManager.authenticate(authRequest);
			SecurityContextHolder.getContext().setAuthentication(authResult);
			// Fire event
		} catch (AuthenticationException failed) {
			unsuccessfulAuthentication(request, response, failed);

		}
	}

	@SuppressWarnings("deprecation")
	private Collection<? extends GrantedAuthority> getGrantedAuthorities(
			String header) {
		StringTokenizer tokenizer = new StringTokenizer(header, ",");
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		while (tokenizer.hasMoreTokens())

		{
			GrantedAuthority gAuth = new GrantedAuthorityImpl(
					dequote(tokenizer.nextToken()));
			grantedAuthorities.add(gAuth);
		}
		return grantedAuthorities;
	}

	/**
	 * Ensures the authentication object in the secure context is set to null
	 * when authentication fails.
	 */
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) {
		SecurityContextHolder.clearContext();

		if (logger.isDebugEnabled()) {
			logger.debug("Cleared security context due to exception", failed);
		}
		request.getSession().setAttribute(
				WebAttributes.AUTHENTICATION_EXCEPTION, failed);
	}

	String dequote(String str) {
		if (str.startsWith("\"")) {
			str = str.substring(1, str.length());
		}
		if (str.endsWith("\"")) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	

	public int getOrder() {

		return 0;
	}


}
