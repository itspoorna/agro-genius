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
import com.bvb.agroGenius.dto.CartProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.CartServices;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartServices cartServices;
	
	@GetMapping("/user/{email}")
	public AgroGeniusResponse getAllCartProductsByemail(@PathVariable(name = "email")String email){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			CartDto dto = cartServices.getAllCartProductsByUserId(email);	
			status = HttpStatus.OK;
			return new AgroGeniusResponse(dto, status);
		}
		catch(AgroGeniusException exception) {
			message =  exception.getLocalizedMessage();
		}catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		
		return new AgroGeniusResponse(message, status);	
	}
	
	@PostMapping("/user/{email}")
	public AgroGeniusResponse addCartProducts(@PathVariable(name = "email")String email,
											@RequestBody CartProductDto dto){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = cartServices.addProduct(email, dto);	
			status = HttpStatus.OK;
		}
		catch(AgroGeniusException exception) {
			message = "Failed to add Item to your cart !!" + exception.getLocalizedMessage();
		}
		catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);	
	}
	
	@DeleteMapping("/user/{email}/product/{productId}")
	public AgroGeniusResponse deleteCartProducts(@PathVariable(name = "email")String email, @PathVariable(name = "productId")Integer productId){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = cartServices.deleteCartProducts(email, productId);	
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
	
	@DeleteMapping("/user/{email}")
	public AgroGeniusResponse deleteCart(@PathVariable(name = "email")String email){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = cartServices.deleteCart(email);	
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
