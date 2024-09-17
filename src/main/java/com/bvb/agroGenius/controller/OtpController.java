package com.bvb.agroGenius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.OtpValidation;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.OtpServices;

@RestController
@RequestMapping("/validator")
public class OtpController {

	@Autowired
	private OtpServices otpServices;
	
	@PostMapping
	public AgroGeniusResponse getOtp(@RequestBody OtpValidation otpValidation) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = otpServices.getOtp(otpValidation);
			status = HttpStatus.OK;
		} catch (Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
	
	@PostMapping("/verify")
	public AgroGeniusResponse validateOtp(@RequestBody OtpValidation otpValidation) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		String message = null;
		try {
			message = otpServices.validateOtp(otpValidation);
			status = HttpStatus.OK;
		} catch(AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}catch (Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
}
