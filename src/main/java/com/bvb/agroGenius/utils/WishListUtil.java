package com.bvb.agroGenius.utils;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.models.Wishlist;

public class WishListUtil {
	
	public static WishlistDto convertWishlistEntityToDto (Wishlist wishlist) {
		WishlistDto dto = new WishlistDto();
		dto.setId(wishlist.getId());
		dto.setUserId(wishlist.getUser().getId());
		dto.setAddedAt(wishlist.getAddedAt());
		dto.setProduct(wishlist.getProduct());
		return dto;
	}
	
	public static Wishlist convertWishlistDtoToEntity (WishlistDto dto) {
		Wishlist wishlist = new Wishlist();
		BeanUtils.copyProperties(dto, wishlist);
		return wishlist;
	}

}
