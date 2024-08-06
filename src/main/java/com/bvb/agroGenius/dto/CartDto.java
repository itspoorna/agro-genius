package com.bvb.agroGenius.dto;

import java.util.List;

public class CartDto {

	private Integer id;
	
	private List<CartItemsDto> cartIitems;
			
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CartItemsDto> getCartIitems() {
		return cartIitems;
	}

	public void setCartIitems(List<CartItemsDto> cartIitems) {
		this.cartIitems = cartIitems;
	}

			
}
