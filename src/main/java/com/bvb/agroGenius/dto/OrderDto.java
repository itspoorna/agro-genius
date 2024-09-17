package com.bvb.agroGenius.dto;

import java.time.LocalDateTime;
import java.util.Set;;

public class OrderDto {

	private Integer id;
	private Double amount;
	private String orderStatus;
	private LocalDateTime createdAt;
	private String razorPayOrderID;
		
	private Set<OrderProductDto> orderProducts;
	
	public String getRazorPayOrderID() {
		return razorPayOrderID;
	}
	public void setRazorPayOrderID(String razorPayOrderID) {
		this.razorPayOrderID = razorPayOrderID;
	}
	public Set<OrderProductDto> getOrderProducts() {
		return orderProducts;
	}
	public void setOrderProducts(Set<OrderProductDto> orderProducts) {
		this.orderProducts = orderProducts;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
