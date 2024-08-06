package com.bvb.agroGenius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartItemsDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.CartServices;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartServices cartServices;
	
	@GetMapping("/{userId}")
	public AgroGeniusResponse getAllCartItems(@PathVariable(name = "userId")Integer userId){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			CartDto dto = cartServices.getAllCartItems(userId);	
			status = HttpStatus.OK;
			return new AgroGeniusResponse(dto, status);
		}
		catch(AgroGeniusException exception) {
			message =  exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);	
	}
	
	@PostMapping("/{userId}")
	public AgroGeniusResponse addCartItem(@PathVariable(name = "userId")Integer userId,
												@RequestBody CartItemsDto cartItems){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = cartServices.addProduct(userId, cartItems);	
			status = HttpStatus.OK;
		}
		catch(AgroGeniusException exception) {
			message = "Failed to add User !!" + exception.getLocalizedMessage();
		}
		catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);	
	}
	
	@DeleteMapping("/{userId}/{itemId}")
	public AgroGeniusResponse deleteCartItems(@PathVariable(name = "userId")Integer userId, @PathVariable(name = "itemId")Integer itemId){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = cartServices.deleteCartItem(userId, itemId);	
			status = HttpStatus.OK;
		}
		catch(AgroGeniusException exception) {
			message = "Failed to add User !!" + exception.getLocalizedMessage();
		}
		catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);	

	}

}
