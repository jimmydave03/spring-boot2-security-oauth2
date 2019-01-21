package com.oauth2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.oauth2.entity.UserProfile;

/**
 * 
 * @author Jimmy Dave.
 *
 */
@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, Integer>{

	public UserProfile findByEmail(String lowercaseLogin);

}
