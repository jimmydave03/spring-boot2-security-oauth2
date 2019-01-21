package com.oauth2.security;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}