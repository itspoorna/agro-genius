package com.bvb.agroGenius.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.service.OrderServices;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderServices orderService;
	
	@GetMapping("/{id}")
	public ResponseEntity<List<OrderDto>> getAllOrdersById(@PathVariable(name = "id") Integer userId){
		return orderService.getOrdersById(userId);
	}

}
