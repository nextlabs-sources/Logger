package com.nextlabs.logger.controller;

import com.nextlabs.logger.config.TokenUtil;
import com.nextlabs.logger.model.AuthenticateRequest;
import com.nextlabs.logger.model.AuthenticateResponse;
import com.nextlabs.logger.service.LoggerUserDetailsService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Sarita Sethy
 */

@RestController
@CrossOrigin
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final TokenUtil tokenUtil;

	private final LoggerUserDetailsService userDetailsService;
	private static final Logger LOG = LogManager.getLogger("com.nextlabs.logger");

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, TokenUtil tokenUtil, LoggerUserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.tokenUtil = tokenUtil;
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	protected ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticateRequest authenticationRequest) {
		ResponseEntity<?> result;
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = tokenUtil.generateToken(userDetails);
			result = ResponseEntity.ok(new AuthenticateResponse(token));
		} catch (DisabledException e) {
			LOG.debug("User does not exist");
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN, "User does not exist", e);
		} catch (BadCredentialsException e) {
			LOG.debug("User has entered wrong password");
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED, "User has entered wrong password", e);
		}
		return result;
	}

	private void authenticate(String username, String password) throws DisabledException, BadCredentialsException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
