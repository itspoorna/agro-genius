package com.bvb.agroGenius.services.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.service.UserService;
import com.bvb.agroGenius.utils.UserUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class UserServicesImplementation implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServicesImplementation.class);
	
	@Autowired
	private UserRepository repository;

	@PersistenceContext
	private EntityManager em;
	
	public User getUserById(Integer id) throws AgroGeniusException {
		User user = null;
		try {
			user = repository.findById(id).get();
			
		} catch(Exception exception) {
			throw new AgroGeniusException(exception.getMessage());
		}
		if(user == null) {
			throw new AgroGeniusException("User not found");
		}
		return user;
	}

	public Set<UserDto> getAllUser() throws AgroGeniusException {
		try {
			Set<UserDto> listOfDto = repository
										.findAll()
										.stream()
										.map(UserUtils::convertUserEntityToDto)
										.collect(Collectors.toSet());
			
			logger.info("List of user data returned successfully");
			return listOfDto;
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException(exception.getLocalizedMessage());
		}
	}

	public String addNewUser(UserDto dto) throws AgroGeniusException {
		User user = UserUtils.convertUserDtoToEntity(dto);
		try {
			repository.save(user);
			
			logger.info(" User {} registered successfully ", dto.getFullName());
			return "User '"+dto.getFullName()+"' created successfully";
			
		} catch (DataIntegrityViolationException exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("User data has been registerd already.." + exception.getLocalizedMessage());
		}
	}

	@Transactional
	public String updateUser(Integer userId, UserDto dto) throws AgroGeniusException {
		
		try {
			User user = UserUtils.convertUserDtoToEntity(dto);
			User existingUser = repository.findById(userId).get();
			
			if (existingUser == null) {
				throw new AgroGeniusException("User not Found");
			}
			
			// Update Name
			if(user.getFullName() != null && user.getFullName() != existingUser.getFullName()) {
				existingUser.setFullName(dto.getFullName());
			}
			// Update Email
			if (user.getEmailId() != null && user.getEmailId().length() > 0
					&& !Objects.equals(user.getEmailId(), existingUser.getEmailId())) {
				existingUser.setEmailId(user.getEmailId());
			}
			
			// Update PhoneNumber
			if (user.getPhoneNumber() != null && user.getPhoneNumber().length() > 0
					&& !Objects.equals(user.getPhoneNumber(), existingUser.getPhoneNumber())) {
				existingUser.setPhoneNumber(user.getPhoneNumber());
			}
			
			// Update Profile pic
			if (user.getProfilePic() != null && user.getProfilePic().length() > 0
					&& !Objects.equals(user.getProfilePic(), existingUser.getProfilePic())) {
				existingUser.setProfilePic(user.getProfilePic());
			}
			
			logger.info("{} updated successfully", existingUser.getFullName());
			
			return "User '"+ existingUser.getFullName()+"' updated successfully";
			
		} catch(Exception exception) {
			throw new AgroGeniusException("Cannot update user data " + exception.getLocalizedMessage());
		}
	}

	public String deleteUser(Integer userId) throws AgroGeniusException {
		Optional<User> user = repository.findById(userId);
		try {
			if(user.isPresent()) {
				repository.deleteById(userId);
				String name = user.get().getFullName();
				
				logger.error("{} data deleted", user.get().getFullName());
				return "User "+name+" Has deleted successfully.";
			}else {
				throw new AgroGeniusException("User doesn't exist.");
			}
		} catch (Exception exception) {
			
			logger.error(exception.getLocalizedMessage());
			throw new AgroGeniusException("User doesn't exist." + exception.getLocalizedMessage());
		} 
	}

	@Override
	public Set<UserDto> getDemo() {
		
		String id = "1 or 1 = 1";
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = ?1", User.class); 
		
		Set<UserDto> user = query
				 .setParameter(1, id) .getResultList().stream().map(UserUtils::convertUserEntityToDto).collect(Collectors.toSet());
		
		return user;
	}
}
