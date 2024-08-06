package com.bvb.agroGenius.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bvb.agroGenius.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
