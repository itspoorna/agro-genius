package com.bvb.agroGenius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.ContactUsDto;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.ContactService;
import com.bvb.agroGenius.service.GoogleCaptchaVerification;

@RestController
@RequestMapping("/contact")
public class ContactController {
	
	@Autowired
	private GoogleCaptchaVerification googleCaptchaVerification;
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping
	public AgroGeniusResponse addContactForm(@RequestBody ContactUsDto contactUsDto) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			if(googleCaptchaVerification.isvalidCaptcha(contactUsDto.getToken())) {
				contactService.saveContactDetails(contactUsDto);
			}
			status = HttpStatus.OK;
		} catch (Exception exception) {
			message = "Failed to send contact data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
}
