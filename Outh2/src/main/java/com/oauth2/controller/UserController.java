package com.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth2.entity.UserProfile;
import com.oauth2.service.UserProfileService;

/**
 * 
 * @author Jimmy Dave
 *
 */
@RestController
@RequestMapping(value="/api/users")
public class UserController {
	
	@Autowired
	private UserProfileService userProfileService;
	
	//Add user to the db.
	@PostMapping("/add")
	public void createUser(@RequestBody UserProfile userProfile) throws Exception {
		userProfileService.createUser(userProfile);
	}
	
	//Get loggedin user detail.
	@GetMapping
	public ResponseEntity<User> getUserDetails() throws Exception {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}