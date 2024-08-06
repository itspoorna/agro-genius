package com.bvb.agroGenius.utils;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.UserDto;
import com.bvb.agroGenius.models.User;

public class UserUtils {
	
	public static UserDto convertUserEntityToDto(User user) {
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}
	
	public static User convertUserDtoToEntity(UserDto dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user;
	}

}
