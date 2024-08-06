 package com.bvb.agroGenius.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.UserService;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	 
	HttpStatus status = HttpStatus.BAD_REQUEST;
	String message = "";
	
	@Autowired
	private UserService services;
	
	@GetMapping("/sqlmapping")
	public Set<UserDto> getDemo(){
		return services.getDemo();
	}
	
	@GetMapping
	public AgroGeniusResponse getAllUsers(){
		
		try {
			Set<UserDto> listOfDto = services.getAllUser();
			if(listOfDto.isEmpty()) {
				throw new AgroGeniusException("No users found");
			}
			status = HttpStatus.OK;
			return new AgroGeniusResponse(listOfDto, status);
		}
		catch(AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);			
	}

	@PostMapping("/demo")
	public User demo(@RequestBody User user) {
		logger.warn(user.toString());
		return user;
	}
	
	
	@PostMapping
	public AgroGeniusResponse addUser(@RequestBody UserDto dto) {
		
		try {
			message = services.addNewUser(dto);	
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
	
	@PutMapping("/id/{userId}")
	public AgroGeniusResponse updateUser(@PathVariable(name = "userId")Integer userId, @RequestBody UserDto userDto) {
		
		try {
			message = services.updateUser(userId, userDto);		
			status = HttpStatus.OK;
		}catch(AgroGeniusException exception) {
			message = "Failed to Update User !!" + exception.getLocalizedMessage();
		}
		catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);	
	}
	
	@DeleteMapping("/id/{userId}")
	public AgroGeniusResponse deleteUser(@PathVariable(name = "userId")Integer userId){
		
		try {
			message = services.deleteUser(userId);		
			status = HttpStatus.OK;
		}catch(AgroGeniusException exception) {
			message = "Failed to add User !!" + exception.getLocalizedMessage();
		}
		catch(Exception exception) {
			message = "Internal Server Error !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
		
	}
}

























