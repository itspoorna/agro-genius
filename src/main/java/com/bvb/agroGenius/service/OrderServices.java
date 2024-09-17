package com.bvb.agroGenius.service;

import java.util.List;

import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Order;

public interface OrderServices {

	List<OrderDto> getOrdersById(String email) throws AgroGeniusException;

	Order createNewOrder(String email, OrderDto orderDto) throws AgroGeniusException;

//	Order updateOrder(Map<String, String> responsePayLoad)  throws AgroGeniusException;
	String updateOrder(OrderDto orderDto)  throws AgroGeniusException;

	String cancelOrderById(Integer orderId) throws AgroGeniusException;

	Order createOrderOnlyForPayment(String email,OrderDto orderDto) throws AgroGeniusException;
}
