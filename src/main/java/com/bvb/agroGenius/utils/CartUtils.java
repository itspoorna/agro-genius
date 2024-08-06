package com.bvb.agroGenius.utils;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartItemsDto;
import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.models.Cart;
import com.bvb.agroGenius.models.CartItems;

public class CartUtils {
	
	public static CartDto convertCartEntityToDto(Cart cart) {
		CartDto dto = new CartDto();
		BeanUtils.copyProperties(cart, dto);
		return dto;
	}
	
	public static Cart convertCartDtoToEntity(CartDto cartDto) {
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartDto, cart);
		return cart;
	}
	
	public static CartItemsDto convertCartItemsEntityToDto(CartItems cartItems) {
		CartItemsDto dto = new CartItemsDto();
		BeanUtils.copyProperties(cartItems, dto);
		
		ProductDto productDto = ProductUtils.convertProductsEntityToDto(cartItems.getProduct());
		dto.setProduct(productDto);
		
		return dto;
	}
	
	public static CartItems convertCartItemsDtoToEntity(CartItemsDto cartItemsDto) {
		CartItems cartItems = new CartItems();
		BeanUtils.copyProperties(cartItemsDto, cartItems);
		return cartItems;
	}

}
