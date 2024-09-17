package com.bvb.agroGenius.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.models.User;



public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String value) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByEmailId(value);
		
		if(user.isEmpty()) {
			throw new UsernameNotFoundException("Could not find user");
		}
		return new AgroGeniusUserDetails(user.get());
	}

}
