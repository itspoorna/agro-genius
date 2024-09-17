package com.bvb.agroGenius.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bvb.agroGenius.models.OtpValidation;

@Repository
public interface OtpRepository extends JpaRepository<OtpValidation, Long>{

	@Query(nativeQuery = true, value = "select * from otp_validation ov where value = ?1 and otp = ?2")
	Optional<OtpValidation> findByValueAndOtp(String value, String otp);

	@Modifying
	@Query(nativeQuery = true, value = "delete from otp_validation where value = ?1")
	int deleteAllByValue(String value);
}
