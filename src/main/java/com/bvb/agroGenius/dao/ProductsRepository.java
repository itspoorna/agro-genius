package com.bvb.agroGenius.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bvb.agroGenius.models.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer>{

	List<Product> findAllByCategoryId(Integer categoryId);

}
