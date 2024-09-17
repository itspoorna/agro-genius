package com.bvb.agroGenius.service;

import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;

public interface CartServices {

	CartDto getAllCartProductsByUserId(String email) throws AgroGeniusException;
	
	String addProduct(String email, CartProductDto dto) throws AgroGeniusException;
	
	String deleteCartProducts(String email, Integer productId) throws AgroGeniusException;
	
	String deleteCart(String email) throws AgroGeniusException;
}
