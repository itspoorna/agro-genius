package com.bvb.agroGenius.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.ProductServices;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

	@Autowired
	private ProductServices productServices;

	@GetMapping("getAll")
	public AgroGeniusResponse getProducts() {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			List<ProductDto> listOfDto = productServices.getProducts();
			status = HttpStatus.OK;
			return new AgroGeniusResponse(listOfDto, status);
		} catch (AgroGeniusException exception) {
			message = exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
	
	@GetMapping("/category/{category}")
	public AgroGeniusResponse getProductByCategory(@PathVariable(name = "category") String category) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			List<ProductDto> listOfDto = productServices.getProductsByCategory(category);
			status = HttpStatus.OK;
			return new AgroGeniusResponse(listOfDto, status);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			message = exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@PostMapping
	public AgroGeniusResponse addProduct(@RequestBody ProductDto dto) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = productServices.addProduct(dto);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to Add Product!!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);

	}

	@PutMapping("/id/{productId}")
	public AgroGeniusResponse updateUser(@PathVariable(name = "productId") Integer productId,
			@RequestBody ProductDto productDto) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = productServices.updateProduct(productId, productDto);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to Update Product data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@DeleteMapping("/id/{productId}")
	public AgroGeniusResponse deleteUser(@PathVariable(name = "productId") Integer productId) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = productServices.deleteProduct(productId);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = exception.getMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
}
