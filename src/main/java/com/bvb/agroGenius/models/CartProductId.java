package com.bvb.agroGenius.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

@Embeddable
public class CartProductId implements Serializable{

	private static final long serialVersionUID = -8619662841388010450L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cart cart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cart, product);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartProductId other = (CartProductId) obj;
		return Objects.equals(cart, other.cart) && Objects.equals(product, other.product);
	}

	
}
