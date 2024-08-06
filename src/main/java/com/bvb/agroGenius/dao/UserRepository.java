package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bvb.agroGenius.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	
}
