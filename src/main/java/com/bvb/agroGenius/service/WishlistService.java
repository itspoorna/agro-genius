package com.bvb.agroGenius.service;

import java.util.List;

import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Wishlist;

public interface WishlistService {

	List<WishlistDto> getAllWishlist(String email) throws AgroGeniusException;
	
	String addNewItemToWishlist(String email, Wishlist wishlist) throws AgroGeniusException;
	
	String removeAnItemFromWishlist(String email, Integer id) throws AgroGeniusException;
	
	String emptyWishlist(String email) throws AgroGeniusException;

}
