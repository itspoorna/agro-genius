package com.bvb.agroGenius.service;

import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.OtpValidation;

public interface OtpServices {

	String getOtp(OtpValidation otpValidation);

	String validateOtp(OtpValidation otpValidation) throws AgroGeniusException;
}
