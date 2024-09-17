package com.bvb.agroGenius.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bvb.agroGenius.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	@Query(nativeQuery = true, value = "select * from orders o left join orders_product op on o.id = op.order_id where o.user_id = ?1")
	List<Order> findAllByUserId(Integer userId);

	Order findByRazorPayOrderID(String razorPayOrderId);
}
