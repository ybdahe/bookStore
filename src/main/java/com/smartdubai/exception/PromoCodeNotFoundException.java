package com.smartdubai.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PromoCodeNotFoundException extends RuntimeException {

	public PromoCodeNotFoundException(String exception) {
		super(exception);
	}

}
