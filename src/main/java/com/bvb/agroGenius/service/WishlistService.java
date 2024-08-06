package com.bvb.agroGenius.service;

import java.util.List;

import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Wishlist;

public interface WishlistService {

	List<WishlistDto> getAllWishlist(Integer userId) throws AgroGeniusException;
	
	String addNewItemToWishlist(Integer userId, Wishlist wishlist) throws AgroGeniusException;
	
	String removeAnItemFromWishlist(Integer userId, Integer id) throws AgroGeniusException;
	
	String emptyWishlist(Integer userId) throws AgroGeniusException;

}
