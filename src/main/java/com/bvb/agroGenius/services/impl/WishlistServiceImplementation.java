package com.bvb.agroGenius.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.WishlistRepository;
import com.bvb.agroGenius.dto.WishlistDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.models.Wishlist;
import com.bvb.agroGenius.service.UserService;
import com.bvb.agroGenius.service.WishlistService;
import com.bvb.agroGenius.utils.WishListUtil;

@Service
public class WishlistServiceImplementation implements WishlistService {

	Logger logger = LoggerFactory.getLogger(WishlistServiceImplementation.class);

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private UserService userService;

	@Override
	public List<WishlistDto> getAllWishlist(Integer userId) throws AgroGeniusException {
		try {
			List<WishlistDto> listOfDto = wishlistRepository.findAllByUserId(userId).stream()
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
	public String addNewItemToWishlist(Integer userId, Wishlist wishlist) throws AgroGeniusException {

		try {
			User user = userService.getUserById(userId);
			if (user == null) {
				throw new AgroGeniusException("User doen't exist");
			}
			if (wishlist == null) {
				throw new AgroGeniusException("Empty item List");
			}

			Integer productId = wishlist.getProduct().getId();

			// User allowed to have a items in wishlist
			Integer count = wishlistRepository.wishListCount(productId, userId);
			if (count > 5) {
				throw new AgroGeniusException("Wishlist size excedes");
			}

			// check if item already exist
			Boolean isExists = wishlistRepository.isProductIdExist(productId, userId);

			if (isExists) {
				throw new AgroGeniusException("Product already exist in the " + user.getFullName() + " wishlist");
			}

			wishlist.setAddedAt(LocalDateTime.now());

			wishlist.setUser(user);

			wishlistRepository.save(wishlist);

			String response = wishlist.getProduct().getProductName() + " added to " + user.getFullName() + " wishlist";
			logger.info(response);
			return response;
		} catch (Exception exception) {

			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	@Override
	public String removeAnItemFromWishlist(Integer userId, Integer id) throws AgroGeniusException {

		try {
			User user = userService.getUserById(userId);
			if(user == null) {
				throw new AgroGeniusException("User not found");
			}
			
			Wishlist wishlist = wishlistRepository.findById(id).get();
			if(wishlist == null) {
				throw new AgroGeniusException("Mentioned wishlist doesn't exist");
			}
			wishlistRepository.deleteById(id);
			
			String response = wishlist.getProduct().getProductName()+" deleted.";
			
			logger.info(response);
			return response;
		} catch(Exception exception) {
			
			logger.error(exception.getClass().toString());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

	@Override
	public String emptyWishlist(Integer userId) throws AgroGeniusException {

		try {
			
			User user = userService.getUserById(userId);
			if(user == null) {
				throw new AgroGeniusException("User not found");
			}
			
			Boolean isUserExist = wishlistRepository.isUserExists(userId);
			if(isUserExist) {
				Integer obj = wishlistRepository.deleteByUserId(userId);
				logger.error(obj.toString());
			}
			return user.getFullName()+ "'s wishlist is empty"; 
		} catch (JpaSystemException exception) {
			return "Items removed successfully";
		} catch(Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

}
