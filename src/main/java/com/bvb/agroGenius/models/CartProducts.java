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
@Table(name = "cart_product")
@AssociationOverrides({ @AssociationOverride(name = "id.cart", joinColumns = @JoinColumn(name = "cart_id")),
		@AssociationOverride(name = "id.product", joinColumns = @JoinColumn(name = "product_id")) })
public class CartProducts implements Serializable {

	private static final long serialVersionUID = 6331884744898486808L;

	@EmbeddedId
	private CartProductId id = new CartProductId();

	private Integer price;

	private Integer quantity;

	public CartProductId getId() {
		return id;
	}

	public void setId(CartProductId id) {
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
	public Cart getCart() {
		return this.getId().getCart();
	}
	
	public void setCart(Cart cart) {
		this.getId().setCart(cart);
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
		CartProducts other = (CartProducts) obj;
		return Objects.equals(id, other.id) && Objects.equals(price, other.price)
				&& Objects.equals(quantity, other.quantity);
	}

}
