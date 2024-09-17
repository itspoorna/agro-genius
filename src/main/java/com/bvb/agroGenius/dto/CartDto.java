package com.bvb.agroGenius.dto;

import java.util.Set;

public class CartDto {

	private Integer id;
	
	private Integer userId;
	
	private Set<CartProductDto> cartProducts;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Set<CartProductDto> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(Set<CartProductDto> cartProducts) {
		this.cartProducts = cartProducts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

			
}
