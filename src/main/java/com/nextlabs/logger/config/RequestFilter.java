package com.nextlabs.logger.config;

import com.nextlabs.logger.service.LoggerUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Sarita Sethy
 */
@Component
public class RequestFilter extends OncePerRequestFilter {

	private final LoggerUserDetailsService loggerUserDetailsService;

	private final TokenUtil tokenUtil;

	@Autowired
	public RequestFilter(LoggerUserDetailsService loggerUserDetailsService, TokenUtil tokenUtil) {
		this.loggerUserDetailsService = loggerUserDetailsService;
		this.tokenUtil = tokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String jwtToken = request.getHeader("Authorization");

		String userName = null;

		if (jwtToken != null) {
			try {
				userName = tokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.debug("Unable to get JWT Token");
				throw new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Unable to retrieve the token", e);
			} catch (ExpiredJwtException e) {
				logger.debug("Token has expired");
				throw new ResponseStatusException(
						HttpStatus.NOT_EXTENDED, "Token has expired", e);
			}
		}

		// validate token
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.loggerUserDetailsService.loadUserByUsername(userName);
			try {
				if (tokenUtil.validateToken(jwtToken, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			} catch (UsernameNotFoundException e) {
				logger.debug(e.getMessage());
				throw new ResponseStatusException(
						HttpStatus.NOT_ACCEPTABLE, "User not found " + userName, e);
			}
		}
		chain.doFilter(request, response);
	}

}
