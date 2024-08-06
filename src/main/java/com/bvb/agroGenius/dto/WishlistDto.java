package com.bvb.agroGenius.dto;

import java.time.LocalDateTime;

import com.bvb.agroGenius.models.Product;

public class WishlistDto {

	private Integer id;
	private Integer userId;
	private Product product;
	private LocalDateTime addedAt;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public LocalDateTime getAddedAt() {
		return addedAt;
	}
	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}	
}
