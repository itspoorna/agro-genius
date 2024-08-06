package com.bvb.agroGenius.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.OrderRepository;
import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.service.OrderServices;
import com.bvb.agroGenius.utils.OrderUtils;

@Service
public class OrderServiceImplementation implements OrderServices{
	
	@Autowired
	private OrderRepository orderRepository;

	public ResponseEntity<List<OrderDto>> getOrdersById(Integer userId) {
		
		List<OrderDto> listOfOrder =  orderRepository
									  .findAll()
									  .stream()
									  .map(OrderUtils::convertOrderEntityToDto)
									  .collect(Collectors.toList());
		if(listOfOrder != null) {
			return ResponseEntity.ok().body(listOfOrder);
		}
		return ResponseEntity.noContent().build();
	}

}
