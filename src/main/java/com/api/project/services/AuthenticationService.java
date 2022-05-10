package com.api.project.services;

import com.api.project.security.UserSS;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationService {

	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
		} catch (Exception e) {
			return null;
		}
	}
}
