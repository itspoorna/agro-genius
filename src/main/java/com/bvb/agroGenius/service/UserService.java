package com.bvb.agroGenius.service;

import java.util.Set;

import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.User;

public interface UserService {
	
	User getUserById(Integer id) throws AgroGeniusException;

	Set<UserDto> getAllUser() throws AgroGeniusException;
	
	String addNewUser(UserDto dto) throws AgroGeniusException;
	
	String updateUser(Integer userId, UserDto dto) throws AgroGeniusException;
	
	String deleteUser(Integer userId) throws AgroGeniusException;

	Set<UserDto> getDemo();
}
