package com.oauth2.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


/**
 * Spring Security logout handler
 */
@Component
public class CustomLogoutSuccessHandler
        extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {


    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "authorization";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        String token = request.getHeader(HEADER_AUTHORIZATION);
        
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {

            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token.split(" ")[1].trim());
            
            if(oAuth2AccessToken != null) {
	            OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
	            if (oAuth2RefreshToken != null) {
	                tokenStore.removeRefreshToken(oAuth2RefreshToken);
	            }
	            if (oAuth2AccessToken != null) {
	                tokenStore.removeAccessToken(oAuth2AccessToken);
	            }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(ContentType.APPLICATION_JSON.toString());
    }

}
