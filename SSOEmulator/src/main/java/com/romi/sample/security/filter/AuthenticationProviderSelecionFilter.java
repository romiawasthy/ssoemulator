package com.romi.sample.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationProviderSelecionFilter extends
		UsernamePasswordAuthenticationFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String sUserID = (String) request.getHeader("iv-user");

		if (sUserID == null) {
			String iv_user = (String) request.getSession().getAttribute(
					"iv-user");
			if (iv_user == null) {
				sUserID = request.getParameter("j_username");
				if (sUserID != null) {

					request.getSession().setAttribute("iv-user", sUserID);
				}
			}
			super.doFilter(request, res, chain);

		} else {
			String iv_user = (String) request.getSession().getAttribute(
					"iv-user");
			if (iv_user == null) {

				request.getSession().setAttribute("iv-user", sUserID);
			}

			chain.doFilter(request, res);

		}
	}

}
