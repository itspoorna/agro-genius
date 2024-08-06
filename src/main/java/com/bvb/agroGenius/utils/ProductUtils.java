package com.bvb.agroGenius.utils;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.models.Product;

public class ProductUtils {

	public static ProductDto convertProductsEntityToDto(Product product) {
		ProductDto dto = new ProductDto();
		BeanUtils.copyProperties(product, dto);
		return dto;
	}
	
	public static Product convertProductDtoToEntity(ProductDto dto) {
		Product product = new Product();
		BeanUtils.copyProperties(dto, product);
		return product;
	}
}
