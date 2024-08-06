package com.bvb.agroGenius.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bvb.agroGenius.dto.OrderDto;

public interface OrderServices {

	ResponseEntity<List<OrderDto>> getOrdersById(Integer userId);
}
