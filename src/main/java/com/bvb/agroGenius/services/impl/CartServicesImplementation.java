package com.bvb.agroGenius.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.CartItemsRepository;
import com.bvb.agroGenius.dao.CartRepository;
import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dto.CartDto;
import com.bvb.agroGenius.dto.CartItemsDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Cart;
import com.bvb.agroGenius.models.CartItems;
import com.bvb.agroGenius.models.Product;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.service.CartServices;
import com.bvb.agroGenius.utils.CartUtils;
import com.bvb.agroGenius.utils.ProductUtils;

@Service
public class CartServicesImplementation implements CartServices {

	Logger logger = LoggerFactory.getLogger(CartServicesImplementation.class);

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Autowired
	private UserRepository userRepository;

	public CartDto getAllCartItems(Integer userId) throws AgroGeniusException {

		try {
			Cart existingCart = cartRepository.findByUserId(userId).get();
			if (existingCart == null) {
				throw new AgroGeniusException("Empty cart");
			}

			List<CartItemsDto> cartItems = cartItemsRepository.findAllByCartId(existingCart.getId()).stream()
					.map(CartUtils::convertCartItemsEntityToDto).collect(Collectors.toList());

			CartDto dto = CartUtils.convertCartEntityToDto(existingCart);
			dto.setCartIitems(cartItems);

			logger.info("Cart data fetched successfully");
			return dto;
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	// Add New Product
	public String addProduct(Integer userId, CartItemsDto dto) throws AgroGeniusException {

		try {
			// Check user validity
			Optional<User> user = userRepository.findById(userId);
			if (user.isEmpty()) {
				
				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User doesn't exist");
			}

			// Find Cart data
			Optional<Cart> existingCart = cartRepository.findByUserId(userId);
			Cart cart = null;
			if (existingCart.isEmpty()) {
				cart = createCart(userId);
			} else {
				cart = existingCart.get();
			}

			if (dto == null) {
				
				logger.error("Empty product list encountered");
				throw new AgroGeniusException("Empty Items cannot be added to cart");
			}

			// check if item already present
			Integer productId = dto.getProduct().getId();
			Integer quantity = dto.getQuantity();

			// If product already exists in the cart then increase the quantity and return
			// the control.
			CartItems cartItem = checkProductExists(productId, quantity);

			if (cartItem != null) {

				String productName = dto.getProduct().getProductName();

				logger.info(" {} Added to cart successfully", productName);
				dto = CartUtils.convertCartItemsEntityToDto(cartItem);

				return dto.getProduct().getProductName() + " Added to cart successfully";
			}

			CartItems cartItems = CartUtils.convertCartItemsDtoToEntity(dto);

			// set cart
			cartItems.setCart(cart);

			// set Product
			Product product = ProductUtils.convertProductDtoToEntity(dto.getProduct());

			cartItems.setProduct(product);

			cartItemsRepository.save(cartItems);

			logger.info(" {} Added to cart successfully", product.getProductName());
			
			dto = CartUtils.convertCartItemsEntityToDto(cartItems);
			return dto.getProduct().getProductName() + " Added to cart successfully";

		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	public String deleteCartItem(Integer userId, Integer itemId) throws AgroGeniusException {
		try {
			CartItems item = cartItemsRepository.findById(itemId).get();
			if (item != null && item.getCart().getUser().getId() == userId) {
				cartItemsRepository.deleteById(itemId);
				
				logger.warn(" Item {} deleted from the cart", item.getProduct().getProductName());
				return "Item '" + item.getProduct().getProductName() + "' deleted successfully";
			}
			throw new AgroGeniusException("Invalid data encountered");

		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("Internal server error!! due to " + exception.getLocalizedMessage());
		}
	}

	// create new cart
	private Cart createCart(Integer userId) {
		User user = userRepository.findById(userId).get();
		if (user == null) {
			return null;
		}
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	// Increment the cart item quantity if product already exists in the cart
	private CartItems checkProductExists(Integer productId, Integer quantity) {

		Optional<CartItems> item = cartItemsRepository.findByProductId(productId);

		// Return the cart data if it already exist.
		if (item.isPresent()) {
			CartItems cartItem = item.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItemsRepository.saveAndFlush(cartItem);
			return cartItem;
		}

		// else return null
		return null;
	}

}
