package com.bvb.agroGenius.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bvb.agroGenius.dto.GoogleRecaptchaResponse;
import com.bvb.agroGenius.service.GoogleCaptchaVerification;

@Service
public class GoogleCaptchaVerificationImplementation implements GoogleCaptchaVerification {

	Logger logger = LoggerFactory.getLogger(GoogleCaptchaVerificationImplementation.class);
	@Override
	public boolean isvalidCaptcha(String token) {

		boolean valid = false;
		if (StringUtils.isNotBlank(token)) {

			try {
				String recaptchaUrl = "https://www.google.com/recaptcha/api/siteverify";
				String secret = "6LewSj4qAAAAAMpDlWOuCH5siai-e4tHxSmTUhAK";
				String url = recaptchaUrl + "?secret=" + secret + "&response=" + token;

				RestTemplate restTemplate = new RestTemplate();

				GoogleRecaptchaResponse googleResponse = restTemplate.getForObject
						(url, GoogleRecaptchaResponse.class);
				valid = googleResponse.isSuccess();
				System.out.println("");
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		return valid;
	}

}
