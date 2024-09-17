package com.bvb.agroGenius.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.CartProductRepository;
import com.bvb.agroGenius.dao.CartRepository;
import com.bvb.agroGenius.dao.ProductsRepository;
import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Cart;
import com.bvb.agroGenius.models.CartProducts;
import com.bvb.agroGenius.models.Product;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.service.CartServices;
import com.bvb.agroGenius.utils.CartUtils;

import jakarta.transaction.Transactional;

@Service
public class CartServicesImplementation implements CartServices {

	Logger logger = LoggerFactory.getLogger(CartServicesImplementation.class);

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartProductRepository cartProductRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductsRepository productsRepository;

	public CartDto getAllCartProductsByUserId(String email) throws AgroGeniusException {

		try {
			Optional<User> user = userRepository.findByEmailId(email);

			if (user.isEmpty()) {

				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User not found");
			}

			Optional<Cart> existingCart = cartRepository.findByUserId(user.get().getId());

			if (existingCart.isEmpty()) {
				throw new AgroGeniusException("Empty cart");
			}

			CartDto dto = CartUtils.convertCartEntityToDto(existingCart.get());

			logger.info("{}", dto);
			logger.info("Cart data fetched successfully");
			return dto;
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	// create new cart
	private Cart createCart(String email) {
		User user = userRepository.findByEmailId(email).get();
		if (user == null) {
			return null;
		}
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	// Add New Product
	public String addProduct(String email, CartProductDto cartProductDto) throws AgroGeniusException {

		try {
			// Check user validity
			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {

				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User doesn't exist");
			}

			// Find Cart data
			Optional<Cart> existingCart = cartRepository.findByUserId(user.get().getId());

			Cart cart = null;
			if (existingCart.isEmpty()) {
				cart = createCart(email);
			} else {
				cart = existingCart.get();
			}

			if (cartProductDto == null) {

				logger.error("Empty cart products found..!");
				throw new AgroGeniusException("Empty cart products found..!");
			}

			// get Product data
			Optional<Product> prod = productsRepository.findById(cartProductDto.getProductId());
			if (prod.isEmpty()) {
				logger.error(" product Not found.");
				throw new AgroGeniusException("product Not found.");
			}
			Product product = prod.get();

			// If product already exists in the cart
			Optional<CartProducts> existingCartProducts = cartProductRepository
					.findByProductIdAndCartId(product.getId(), cart.getId());

			if (existingCartProducts.isPresent()) {

				CartProducts existingProduct = existingCartProducts.get();
				existingProduct.setQuantity(existingProduct.getQuantity() + cartProductDto.getQuantity());
				existingProduct.setPrice(existingProduct.getPrice() + cartProductDto.getPrice());
				cartProductRepository.saveAndFlush(existingProduct);

				String message = product.getName() + " added to " + user.get().getFullName() + "'s cart";

				logger.info(message);
				return message;
			}

			// If product doesn't exist then only add
			CartProducts cartProducts = CartUtils.convertCartProductsDtoToEntity(cartProductDto);

			cartProducts.setCart(cart);
			cartProducts.setProduct(product);

			cartProductRepository.save(cartProducts);

			String message = product.getName() + " added to " + user.get().getFullName() + "'s cart";

			logger.info(message);
			return message;

		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	@Transactional
	@Override
	public String deleteCartProducts(String email, Integer productId) throws AgroGeniusException {
		try {
			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {
				logger.error("User not found");
				throw new AgroGeniusException("User not found");
			}

			Optional<Cart> cart = cartRepository.findByUserId(user.get().getId());
			if (cart.isEmpty()) {
				logger.error("Cart is already Empty");
				throw new AgroGeniusException("Cart is already Empty");
			}

			Optional<CartProducts> cartProduct = cartProductRepository.findByProductIdAndCartId(productId, productId);
			if (cartProduct.isEmpty()) {
				logger.error("Product doesn't exist in " + user.get().getFullName() + " cart");
				throw new AgroGeniusException("Product doesn't exist in " + user.get().getFullName() + " cart");
			}

			cartProductRepository.deleteByProductIdAndCartId(productId, cart.get().getId());

			logger.info("Product Remove from " + user.get().getFullName() + " cart");
			return "Product Remove from " + user.get().getFullName() + " cart";
		} catch (Exception exception) {

			logger.error("Failed to remove product from cart!! due to " + exception.getLocalizedMessage());
			throw new AgroGeniusException(
					"Failed to remove product from cart!! due to " + exception.getLocalizedMessage());
		}
	}

	@Override
	public String deleteCart(String email) throws AgroGeniusException {
		try {
			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {
				logger.error("User not found");
				throw new AgroGeniusException("User not found");
			}

			Optional<Cart> cart = cartRepository.findByUserId(user.get().getId());
			if (cart.isEmpty()) {
				logger.error("Cart is already Empty");
				throw new AgroGeniusException("Cart is already Empty");
			}
			
			cartRepository.deleteById(cart.get().getId());
			
			logger.info("Cart deleted successfully");
			return "Cart deleted successfully";
		} catch (Exception exception) {
			logger.error("Failed to remove product from cart!! due to " + exception.getLocalizedMessage());
			throw new AgroGeniusException(
					"Failed to remove product from cart!! due to " + exception.getLocalizedMessage());
		}
	}

}
