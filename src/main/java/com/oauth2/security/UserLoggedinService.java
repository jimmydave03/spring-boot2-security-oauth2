package com.oauth2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oauth2.entity.UserProfile;
import com.oauth2.repository.UserProfileRepository;

@Service("userDetailsService")
public class UserLoggedinService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserLoggedinService.class);

    @Autowired
    private UserProfileRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {

        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        UserProfile userFromDatabase = userRepository.findByEmail(lowercaseLogin);

        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else if (!userFromDatabase.isEnabled()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not activated");
        }
        
        return new User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), userFromDatabase.getAuthorities());

    }

}
