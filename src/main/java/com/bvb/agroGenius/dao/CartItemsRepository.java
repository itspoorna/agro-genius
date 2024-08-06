package com.bvb.agroGenius.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bvb.agroGenius.models.CartItems;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer>{
	
	@Query(nativeQuery = true, value = "select * from cart_items ci where ci.cart_id = :cartId")
	List<CartItems> findAllByCartId(@Param("cartId") Integer cartId);

	@Query(nativeQuery = true, value = "select * from cart_items ci where ci.product_id = :productId")
	Optional<CartItems> findByProductId(@Param("productId") Integer productId);
}
