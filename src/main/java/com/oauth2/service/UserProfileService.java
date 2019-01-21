package com.oauth2.service;

import com.oauth2.entity.UserProfile;

public interface UserProfileService {

	void createUser(UserProfile userProfile) throws Exception;

	Boolean verifyEmail(String email);
	

}
