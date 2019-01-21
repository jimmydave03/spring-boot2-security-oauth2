package com.oauth2.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author Jimmy Dave
 *
 */
public enum Roles implements GrantedAuthority {
	USER,
	ADMIN;

	public String getAuthority() {
		return this.name();
	}
	
}

