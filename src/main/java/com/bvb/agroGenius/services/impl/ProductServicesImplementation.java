package com.bvb.agroGenius.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.ProductsRepository;
import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Product;
import com.bvb.agroGenius.service.ProductServices;
import com.bvb.agroGenius.utils.ProductUtils;

import jakarta.transaction.Transactional;

@Service
public class ProductServicesImplementation implements ProductServices {
	
	Logger logger = LoggerFactory.getLogger(ProductServicesImplementation.class);

	@Autowired
	private ProductsRepository productsRepository;

	public List<ProductDto> getProducts() throws AgroGeniusException {
		
		try {
			List<ProductDto> listOfDto = productsRepository
										 	.findAll()
										 	.stream()
										 	.map(ProductUtils::convertProductsEntityToDto)
										 	.collect(Collectors.toList());
			logger.info("List of Products returned successfully.");
			return listOfDto;
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("Internal server error while fetching Product data.");
		}
	}

	public String addProduct(ProductDto dto) throws AgroGeniusException {
		
		try {
			Product product = ProductUtils.convertProductDtoToEntity(dto);

			if (product == null) {
				throw new Exception("Empty data is not allowed");
			}

			productsRepository.save(product);
			logger.info("{} has successfully added to database", dto.getProductName());
			
			return "Product '" + dto.getProductName() + "' has successfully inserted.";
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("Internal server error" + exception.getMessage());
		}
	}

	@Transactional
	public String updateProduct(Integer productId, ProductDto dto) throws AgroGeniusException {

		try {

			Product product = productsRepository.findById(productId).get();
			if (product == null) {
				throw new Exception("Empty product data cannot be updated..!");
			}

			if (dto.getProductName() != null && dto.getProductName() != product.getProductName()) {
				product.setProductName(dto.getProductName());
			}

			if (dto.getQuantity() != null && dto.getQuantity() > 0 && dto.getQuantity() != product.getQuantity()) {
				product.setQuantity(dto.getQuantity());
			}

			if (dto.getPrice() != null && dto.getPrice() > 0 && dto.getPrice() != product.getPrice()) {
				product.setPrice(dto.getPrice());
			}

			if (dto.getCategory() != null && dto.getCategory().length() > 0
					&& dto.getCategory() != product.getCategory()) {
				product.setCategory(dto.getCategory());
			}

			logger.info("{} has updated successfully", product.getProductName());
			return "Product '" + product.getProductName() + "' has successfully updated.";
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("Internal server error.. : " + exception.getMessage());
		}
	}

	public String deleteProduct(Integer productId) throws AgroGeniusException {
		
		try {
			Product product = productsRepository.findById(productId).get();
			
			if (product != null) {
				productsRepository.deleteById(productId);
				logger.warn("{} deleted", product.getProductName());
				return "Product '" + product.getProductName() + "' has deleted successfully..";
			}
			throw new Exception("Product doesn't exists..!!");
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("Internal server error..!" + exception.getMessage());
		}
	}

}
