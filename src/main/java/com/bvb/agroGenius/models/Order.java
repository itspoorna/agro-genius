package com.bvb.agroGenius.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "order_status")
	private String orderStatus;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "razor_pay_order_id")
	private String razorPayOrderID;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "id.order")
	private Set<OrderProduct> orderProducts = new HashSet<>();
	
	public String getRazorPayOrderID() {
		return razorPayOrderID;
	}

	public void setRazorPayOrderID(String razorPayOrderID) {
		this.razorPayOrderID = razorPayOrderID;
	}

	public Set<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(Set<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id){
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, createdAt, id, orderStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(amount, other.amount) && Objects.equals(createdAt, other.createdAt)
				&& Objects.equals(id, other.id) && Objects.equals(orderStatus, other.orderStatus);
	}

}
