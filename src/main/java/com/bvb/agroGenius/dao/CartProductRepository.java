package com.bvb.agroGenius.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bvb.agroGenius.models.CartProductId;
import com.bvb.agroGenius.models.CartProducts;

public interface CartProductRepository extends JpaRepository<CartProducts, CartProductId> {
	
	@Query(nativeQuery = true, value = "select * from cart_product cp where cp.product_id = ?1 and cp.cart_id = ?2")
	Optional<CartProducts> findByProductIdAndCartId(Integer productId, Integer userId);

	@Modifying
	@Query(nativeQuery = true, value = "delete from cart_product where product_id = ?1 and cart_id = ?2")
	long deleteByProductIdAndCartId(Integer productId, Integer cartId);

}
