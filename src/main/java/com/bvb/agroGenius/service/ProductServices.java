package com.bvb.agroGenius.service;

import java.util.List;

import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;

public interface ProductServices {

	List<ProductDto> getProducts() throws AgroGeniusException;
	
	String addProduct(ProductDto dto) throws AgroGeniusException;
	
	String updateProduct(Integer productId, ProductDto dto) throws AgroGeniusException;
	
	String deleteProduct(Integer productId) throws AgroGeniusException;
	
}
