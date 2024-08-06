package com.bvb.agroGenius.service;

import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartItemsDto;
import com.bvb.agroGenius.exception.AgroGeniusException;

public interface CartServices {

	CartDto getAllCartItems(Integer userId) throws AgroGeniusException;
	
	String addProduct(Integer userId, CartItemsDto dto) throws AgroGeniusException;
	
	String deleteCartItem(Integer userId, Integer itemId) throws AgroGeniusException;
}
