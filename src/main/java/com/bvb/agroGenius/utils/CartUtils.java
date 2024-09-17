package com.bvb.agroGenius.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartProductDto;
import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.models.Cart;
import com.bvb.agroGenius.models.CartProducts;

public class CartUtils {
	
	public static CartDto convertCartEntityToDto(Cart cart) {
		CartDto dto = new CartDto();
		
		Set<CartProductDto> cartProductDtos = cart.getCartProducts()
												.stream()
												.map(CartUtils::convertCartProductsEntityToDto)
												.collect(Collectors.toSet());
		dto.setUserId(cart.getUser().getId());
		dto.setCartProducts(cartProductDtos);
		
		BeanUtils.copyProperties(cart, dto);
		return dto;
	}
	
	public static Cart convertCartDtoToEntity(CartDto cartDto) {
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartDto, cart);
		return cart;
	}
	
	public static CartProductDto convertCartProductsEntityToDto(CartProducts cartProduct) {
		CartProductDto dto = new CartProductDto();
		BeanUtils.copyProperties(cartProduct, dto);
		
		ProductDto productDto = ProductUtils.convertProductsEntityToDto(cartProduct.getProduct());
		dto.setProduct(productDto);
		
		return dto;
	}
	
	public static CartProducts convertCartProductsDtoToEntity(CartProductDto cartProductDto) {
		CartProducts cartProducts = new CartProducts();
		BeanUtils.copyProperties(cartProductDto, cartProducts);
		return cartProducts;
	}

}
