package com.bvb.agroGenius.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dao.WishlistRepository;
import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.models.Wishlist;
import com.bvb.agroGenius.service.WishlistService;
import com.bvb.agroGenius.utils.WishListUtil;

@Service
public class WishlistServiceImplementation implements WishlistService {

	Logger logger = LoggerFactory.getLogger(WishlistServiceImplementation.class);

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<WishlistDto> getAllWishlist(String email) throws AgroGeniusException {
		try {
			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {
				throw new AgroGeniusException("User not found");
			}
			List<WishlistDto> listOfDto = wishlistRepository.findAllByUserId(user.get().getId()).stream()
					.map(WishListUtil::convertWishlistEntityToDto).collect(Collectors.toList());
			if (listOfDto == null) {
				throw new AgroGeniusException("Empty wishlist");
			}
			return listOfDto;
		} catch (Exception exception) {
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	@Override
	public String addNewItemToWishlist(String email, Wishlist wishlist) throws AgroGeniusException {

		try {
			Optional<User> existingUser = userRepository.findByEmailId(email);

			if (existingUser.isEmpty()) {
				throw new AgroGeniusException("User doen't exist");
			}
			if (wishlist == null) {
				throw new AgroGeniusException("Empty item List");
			}

			User user = existingUser.get();

			Integer productId = wishlist.getProduct().getId();

			// User allowed to have a items in wishlist
			Integer count = wishlistRepository.wishListCount(productId, user.getId());
			if (count > 5) {
				throw new AgroGeniusException("Wishlist size excedes");
			}

			// check if item already exist
			Boolean isExists = wishlistRepository.isProductIdExist(productId, user.getId());

			if (isExists) {
				throw new AgroGeniusException("Product already exist in the " + user.getFullName() + " wishlist");
			}

			wishlist.setAddedAt(LocalDateTime.now());

			wishlist.setUser(user);

			wishlistRepository.save(wishlist);

			String response = wishlist.getProduct().getName() + " added to " + user.getFullName() + " wishlist";
			logger.info(response);
			return response;
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	@Override
	public String removeAnItemFromWishlist(String email, Integer id) throws AgroGeniusException {

		try {
			Optional<User> existingUser = userRepository.findByEmailId(email);

			if (existingUser.isEmpty()) {
				throw new AgroGeniusException("User doen't exist");
			}

			Wishlist wishlist = wishlistRepository.findById(id).get();
			if (wishlist == null) {
				throw new AgroGeniusException("Mentioned wishlist doesn't exist");
			}
			wishlistRepository.deleteById(id);

			String response = wishlist.getProduct().getName() + " deleted.";

			logger.info(response);
			return response;
		} catch (Exception exception) {

			logger.error(exception.getClass().toString());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

	@Override
	public String emptyWishlist(String email) throws AgroGeniusException {

		try {

			Optional<User> existingUser = userRepository.findByEmailId(email);

			if (existingUser.isEmpty()) {
				throw new AgroGeniusException("User doen't exist");
			}

			Boolean isUserExist = wishlistRepository.isUserExists(existingUser.get().getId());
			if (isUserExist) {
				Integer obj = wishlistRepository.deleteByUserId(existingUser.get().getId());
				logger.error(obj.toString());
			}
			return existingUser.get().getFullName() + "'s wishlist is empty";
		} catch (JpaSystemException exception) {
			return "Items removed successfully";
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

}
