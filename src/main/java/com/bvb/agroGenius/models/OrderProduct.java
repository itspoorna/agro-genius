package com.bvb.agroGenius.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders_product")
@AssociationOverrides({ @AssociationOverride(name = "id.order",
joinColumns = @JoinColumn(name = "order_id")),
@AssociationOverride(name = "id.product", joinColumns = @JoinColumn(name= "product_id")) })
public class OrderProduct implements Serializable{

	private static final long serialVersionUID = -6127078488644494749L;

	@EmbeddedId
	private OrderProductId id = new OrderProductId();
	
	private Integer price;
	
	private Integer quantity;

	public OrderProductId getId() {
		return id;
	}

	public void setId(OrderProductId id) {
		this.id = id;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Transient
	public Order getOrder() {
		return this.getId().getOrder();
	}
	
	public void setOrder(Order order) {
		this.getId().setOrder(order);
	}
	
	@Transient
	public Product getProduct() {
		return this.getId().getProduct();
	}
	
	public void setProduct(Product product) {
		this.getId().setProduct(product);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, price, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderProduct other = (OrderProduct) obj;
		return Objects.equals(id, other.id) && Objects.equals(price, other.price)
				&& Objects.equals(quantity, other.quantity);
	}
	
	
}
