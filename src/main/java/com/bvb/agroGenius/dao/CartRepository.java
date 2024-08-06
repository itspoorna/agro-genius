package com.bvb.agroGenius.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bvb.agroGenius.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	@Query(nativeQuery = true, value = "select * from cart c where c.user_id  = ?1")
	Optional<Cart> findByUserId(Integer userId);
}