package com.bvb.agroGenius.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.OtpRepository;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.OtpValidation;
import com.bvb.agroGenius.service.OtpServices;

import jakarta.transaction.Transactional;

@Service
public class OtpServiceImplementation implements OtpServices {

	Logger logger = LoggerFactory.getLogger(OtpServiceImplementation.class);
	
	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private OtpRepository otpRepository;

	@Override
	public String getOtp(OtpValidation otpValidation) {
		try {
			Integer num = (int) Math.floor(Math.random() * 999999 + 1);
			String otp = num.toString();
			otpValidation.setOtp(otp);
			otpValidation.setCreatedAt(LocalDateTime.now());
			otpValidation.setExpiresAt(LocalDateTime.now().plus(Duration.ofMinutes(10L)));

			otpRepository.save(otpValidation);

			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(otpValidation.getValue());
			message.setSubject("Verify your email");
			message.setText("Welcome to Agro Genius,Your One Time Password is'" + otp
					+ "' .Verify your email Id using the given otp value");
			emailSender.send(message);

			return "Otp is sent to " + otpValidation.getValue() + " successfully";

		} catch (Exception e) {
			return "failed to fetch the otp" + e.getLocalizedMessage();
		}
	}

	@Transactional
	@Override
	public String validateOtp(OtpValidation otpValidation) throws AgroGeniusException {
		try {
			if (otpValidation == null) {
				throw new AgroGeniusException("Enter Valid Otp");
			}
			
			Optional<OtpValidation> existingObj = otpRepository.findByValueAndOtp(otpValidation.getValue(),
					otpValidation.getOtp());
			
			if (existingObj.isEmpty()) {
				throw new AgroGeniusException("Enter Valid Otp");
			}
			otpRepository.deleteAllByValue(otpValidation.getValue());
			return "user email verified successfully";

		} catch (Exception e) {
			logger.error("Enter Valid Otp "+e.getLocalizedMessage());
			throw new AgroGeniusException("Enter Valid Otp");
		}
	}

}
