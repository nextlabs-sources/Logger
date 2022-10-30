package com.nextlabs.logger.model;

import java.io.Serializable;

/**
 * @author Sarita Sethy
 */
public class AuthenticateResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AuthenticateResponse(String token) {
		this.token = token;
	}

}
