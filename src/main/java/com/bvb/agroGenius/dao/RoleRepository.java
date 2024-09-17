package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvb.agroGenius.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findByName(String string);

}
