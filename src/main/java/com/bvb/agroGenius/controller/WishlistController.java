package com.bvb.agroGenius.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Wishlist;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.WishlistService;


@RestController
@RequestMapping("/wishlist")
public class WishlistController {
	
	Logger logger = LoggerFactory.getLogger(WishlistController.class);

	
	
	@Autowired
	private WishlistService wishlistService;
	
	@GetMapping("user/{userId}")
	public AgroGeniusResponse getWishlists(@PathVariable(name = "userId") Integer userId) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			List<WishlistDto> listofDtos= wishlistService.getAllWishlist(userId);
			status = HttpStatus.OK;
			if(listofDtos.isEmpty()) {
				throw new AgroGeniusException("Empty wishlist");
			}
			return new AgroGeniusResponse(listofDtos, status);
		} catch(AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);		
	}
	
	@PostMapping("user/{userId}")
	public AgroGeniusResponse addNewItemToWishlist(@PathVariable(name = "userId") Integer userId,@RequestBody Wishlist wishlist) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = wishlistService.addNewItemToWishlist(userId, wishlist);
			status = HttpStatus.OK;
		} catch(AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);	
	}
	
	@DeleteMapping("user/{userId}/wishlist/{id}")
	public AgroGeniusResponse removeAnItemFromWishlist(@PathVariable(name = "userId")Integer userId, @PathVariable(name = "id") Integer id) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = wishlistService.removeAnItemFromWishlist(userId, id);
			status = HttpStatus.OK;
		} catch(AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);	
	}
	
	@DeleteMapping("user/{userId}")
	public AgroGeniusResponse emptyWishlist(@PathVariable(name = "userId") Integer userId) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = wishlistService.emptyWishlist(userId);
			status = HttpStatus.OK;
		} catch(AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);	
	}
	
	
}
