 package com.bvb.agroGenius.rest;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class AgroGeniusResponse extends ResponseEntity<Object> {

	public AgroGeniusResponse(Object body, HttpStatusCode status) {
		super(body, status);
	}
	
}