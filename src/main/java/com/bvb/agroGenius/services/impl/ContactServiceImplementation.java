package com.bvb.agroGenius.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.ContactRepository;
import com.bvb.agroGenius.dto.ContactUsDto;
import com.bvb.agroGenius.models.ContactUs;
import com.bvb.agroGenius.service.ContactService;

@Service
public class ContactServiceImplementation implements ContactService {

	Logger logger = LoggerFactory.getLogger(ContactServiceImplementation.class);

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public void saveContactDetails(ContactUsDto contactUsDto) {
		try {
			ContactUs contactUs = new ContactUs();
			BeanUtils.copyProperties(contactUsDto, contactUs);

			contactRepository.save(contactUs);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

}
