package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvb.agroGenius.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	Category findByName(String category);

}
