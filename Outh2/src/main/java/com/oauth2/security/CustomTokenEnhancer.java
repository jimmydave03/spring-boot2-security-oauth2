package com.oauth2.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.oauth2.entity.UserProfile;
import com.oauth2.repository.UserProfileRepository;

/**
 * 
 * @author Jimmy Dave.
 *
 */
@Component
public class CustomTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		User user = (User) authentication.getPrincipal();
		
		UserProfile profile = userProfileRepository.findByEmail(user.getUsername());
		
		final Map<String, Object> additionalInfo = new HashMap<>();
		
		additionalInfo.put("firstName", profile.getFirstName());
		additionalInfo.put("lastName", profile.getLastName());
        additionalInfo.put("email", profile.getEmail());
        additionalInfo.put("authorities", user.getAuthorities());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		
		return accessToken;
	}

}
