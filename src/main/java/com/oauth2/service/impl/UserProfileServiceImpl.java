package com.oauth2.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oauth2.entity.UserProfile;
import com.oauth2.enums.Roles;
import com.oauth2.repository.UserProfileRepository;
import com.oauth2.service.UserProfileService;

/**
 * 
 * @author Jimmy Dave.
 *
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public void createUser(UserProfile userProfile) throws Exception {
		
		userProfile.setRole(Roles.USER);
		userProfile.setUpdatedTime(new Date());
		userProfile.setPassword(encoder.encode(userProfile.getPassword()));
		userProfile.setStatus(true);
		
		userProfileRepository.save(userProfile);
	}

	@Override
	public Boolean verifyEmail(String email) {
		UserProfile profile = userProfileRepository.findByEmail(email);	
		
		if(profile == null)
			return false;
		
		return true;
	}
}
