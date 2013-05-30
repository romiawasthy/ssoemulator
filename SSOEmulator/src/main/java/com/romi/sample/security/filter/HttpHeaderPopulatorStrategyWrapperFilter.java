/**
 * 
 */
package com.romi.sample.security.filter;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.web.filter.GenericFilterBean;

import com.romi.sample.security.HTTPHeaderPopulator;

/**
 * @author rawsthy
 * 
 */
public class HttpHeaderPopulatorStrategyWrapperFilter extends GenericFilterBean {
	private Logger logger = org.slf4j.LoggerFactory
			.getLogger(HttpHeaderPopulatorStrategyWrapperFilter.class);
	private HTTPHeaderPopulator customHeaderPopulator;

	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
	}

	/**
	 * 
	 */
	public int getOrder() {
		return 0;
	}

	public HTTPHeaderPopulator getCustomHeaderPopulator() {
		return customHeaderPopulator;
	}

	public void setCustomHeaderPopulator(
			HTTPHeaderPopulator customHeaderPopulator) {
		this.customHeaderPopulator = customHeaderPopulator;
	}

	protected boolean headersRequired() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		boolean switchedUser = false;
		boolean required = false;
		if (authentication != null) {
			Iterator<GrantedAuthority> authorities = (Iterator<GrantedAuthority>) authentication
					.getAuthorities().iterator();
			while (authorities.hasNext()) {
				GrantedAuthority grantedAuthority = authorities.next();
				if (grantedAuthority instanceof SwitchUserGrantedAuthority) {
					switchedUser = true;
					// exit immediately if user is impersonating
					// you could potentially return false here as we don't need
					// to do any more checking for anything
					break;
				}
			}
			required = authentication.getClass().isAssignableFrom(
					UsernamePasswordAuthenticationToken.class);
		}
		return required && !switchedUser && customHeaderPopulator != null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		logger.debug(">>> Executing HttpHeaderPopulatorStrategyWrapperFilter");
		if (headersRequired()) {

			logger.debug(">>> HttpHeaderPopulatorStrategyWrapperFilter - Allowing to proceed to set headers");
			request = customHeaderPopulator.addHeaders(
					(HttpServletRequest) request,
					(HttpServletResponse) response);
		}
		chain.doFilter(request, response);

	}

}
