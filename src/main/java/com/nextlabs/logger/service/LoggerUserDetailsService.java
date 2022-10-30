package com.nextlabs.logger.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Sarita Sethy
 */
@Service
public class LoggerUserDetailsService implements UserDetailsService {

	@Autowired
	private Environment env;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		String bcryptPassword = getUserPassword(userName);
		if (bcryptPassword != null) {
			return new User(userName, bcryptPassword, new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + userName);
		}
	}

	private String getUserPassword(String applicationName) {
		if (applicationName != null) {
			switch (applicationName) {
			case "rmdwindows":
				return env.getProperty("application.rmd.windows.password");
			case "rmdmac":
				return env.getProperty("application.rmd.mac.password");
			case "android":
				return env.getProperty("application.rmd.android.password");
			case "ios":
				return env.getProperty("application.rmd.ios.password");
			default:
				return null;
			}
		}
		return null;
	}
}
