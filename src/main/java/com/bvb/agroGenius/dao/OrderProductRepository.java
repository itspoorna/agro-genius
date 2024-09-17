package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvb.agroGenius.models.OrderProduct;
import com.bvb.agroGenius.models.OrderProductId;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId>{
	

}
