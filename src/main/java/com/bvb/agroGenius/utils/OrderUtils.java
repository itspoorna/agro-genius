package com.bvb.agroGenius.utils;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.dto.OrderItemsDto;
import com.bvb.agroGenius.models.Order;
import com.bvb.agroGenius.models.OrderItems;

public class OrderUtils {

	public static OrderDto convertOrderEntityToDto(Order order) {
		OrderDto dto = new OrderDto();
		BeanUtils.copyProperties(order, dto);
		return dto;
	}
	
	public static Order convertOrderDtoToEntity(OrderDto dto) {
		Order order = new Order();
		BeanUtils.copyProperties(dto, order);
		return order;
	}
	
	public static OrderItemsDto convertOrderEntityToDto(OrderItems orderItems) {
		OrderItemsDto dto = new OrderItemsDto();
		BeanUtils.copyProperties(orderItems, dto);
		return dto;
	}
	
	public static OrderItems convertOrderItemsDtoToEntity(OrderItemsDto dto) {
		OrderItems orderItems = new OrderItems();
		BeanUtils.copyProperties(dto, orderItems);
		return orderItems;
	}
}
