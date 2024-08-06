package com.bvb.agroGenius.dto;

import com.bvb.agroGenius.models.Order;
import com.bvb.agroGenius.models.Product;

public class OrderItemsDto {
	
	private Integer id;
	private Order order;
	private Product product;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	

}
