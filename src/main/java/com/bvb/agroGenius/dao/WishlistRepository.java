package com.bvb.agroGenius.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bvb.agroGenius.models.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer>{
	
	List<Wishlist> findAllByUserId(Integer userId);
	
	@Query(nativeQuery = true, value = "select exists (select * from wishlist w where w.product_id = ?1 and w.user_id = ?2)")
	Boolean isProductIdExist(Integer productId, Integer userId); 

	@Query(nativeQuery = true, value = "select count(id) from wishlist w where w.product_id = ?1 and w.user_id = ?2")
	Integer wishListCount(Integer productId, Integer userId);
	
	@Query(nativeQuery = true, value = "delete from wishlist w where w.user_id = ?1")
	Integer deleteByUserId(Integer userId);
	
	@Query(nativeQuery = true, value = "select exists (select * from wishlist w where w.user_id = ?1)")
	Boolean isUserExists(Integer userId);
}
