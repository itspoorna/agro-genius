package com.bvb.agroGenius.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.dto.OrderProductDto;
import com.bvb.agroGenius.dto.ProductDto;
import com.bvb.agroGenius.models.Order;
import com.bvb.agroGenius.models.OrderProduct;

public class OrderUtils {

	public static OrderDto convertOrderEntityToDto(Order order) {
		OrderDto dto = new OrderDto();
		
		Set<OrderProductDto> orderProductDtos = order.getOrderProducts()
									.stream()
									.map(OrderUtils::convertOrderProductEntityToDto)
									.collect(Collectors.toSet());
		
		dto.setOrderProducts(orderProductDtos);
		BeanUtils.copyProperties(order, dto);
		return dto;
	}
	
	public static Order convertOrderDtoToEntity(OrderDto dto) {
		Order order = new Order();
		BeanUtils.copyProperties(dto, order);
		return order;
	}
	
	public static OrderProduct convertOrderProductDtoToEntity(OrderProductDto dto) {
		OrderProduct orderProduct = new OrderProduct();
		BeanUtils.copyProperties(dto, orderProduct);
		return orderProduct;
	}
	
	public static OrderProductDto convertOrderProductEntityToDto(OrderProduct orderProduct) {
		OrderProductDto dto = new OrderProductDto();
		
		BeanUtils.copyProperties(orderProduct, dto);
		
		ProductDto productDto = ProductUtils.convertProductsEntityToDto(orderProduct.getProduct());
		dto.setProductDto(productDto);
		
		dto.setOrderId(orderProduct.getOrder().getId());
		return dto;
	}
}
