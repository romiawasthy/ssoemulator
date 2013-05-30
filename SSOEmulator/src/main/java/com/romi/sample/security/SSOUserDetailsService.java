/**
 * 
 */
package com.romi.sample.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * R
 * 
 * @author Romi
 * 
 */
@Service
public class SSOUserDetailsService implements AuthenticationUserDetailsService
{
	

	@Override
	public User loadUserDetails(Authentication token) throws UsernameNotFoundException
	{
		

		return new User(token.getName(),"NA", true, true,true,true,  (Collection<GrantedAuthority>) token.getAuthorities());
	}
}
