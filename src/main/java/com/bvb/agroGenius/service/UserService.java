package com.bvb.agroGenius.service;

import java.util.Set;

import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;

public interface UserService {
	
	String signIn(UserDto userDto);
	
	User getUserById(String emailId) throws AgroGeniusException;

	Set<UserDto> getAllUser() throws AgroGeniusException;
	
	String addNewUser(UserDto dto) throws AgroGeniusException;
	
	String updateUser(String emailId, UserDto dto) throws AgroGeniusException;
	
	String deleteUser(String emailId) throws AgroGeniusException;

	Set<UserDto> getDemo();

	String signInWithGoogle(String accessToken) throws AgroGeniusException;

}
