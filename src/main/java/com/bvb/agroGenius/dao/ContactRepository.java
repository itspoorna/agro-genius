package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvb.agroGenius.models.ContactUs;

public interface ContactRepository extends JpaRepository<ContactUs, Long>{

}
