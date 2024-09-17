package com.bvb.agroGenius.services.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bvb.agroGenius.dao.OrderProductRepository;
import com.bvb.agroGenius.dao.OrderRepository;
import com.bvb.agroGenius.dao.ProductsRepository;
import com.bvb.agroGenius.dao.UserRepository;
import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.dto.OrderProductDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Order;
import com.bvb.agroGenius.models.OrderProduct;
import com.bvb.agroGenius.models.Product;
import com.bvb.agroGenius.models.User;
import com.bvb.agroGenius.service.OrderServices;
import com.bvb.agroGenius.utils.OrderUtils;
import com.razorpay.RazorpayClient;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImplementation implements OrderServices {

	Logger logger = LoggerFactory.getLogger(OrderServiceImplementation.class);

	@Value("${razorpay.key.id}")
	private String razorPayKey;

	@Value("${razorpay.secret.key}")
	private String razorPaySecret;

	// to communicate with razorPay- predefined
	private RazorpayClient client;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	public List<OrderDto> getOrdersById(String email) throws AgroGeniusException {

		try {
			Optional<User> user = userRepository.findByEmailId(email);

			if (user.isEmpty()) {

				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User not found");
			}

			List<OrderDto> listOfOrders = orderRepository.findAllByUserId(user.get().getId()).stream()
					.map(OrderUtils::convertOrderEntityToDto).collect(Collectors.toList());

			logger.info("Order List of {} sent successfully", user.get().getFullName());
			return listOfOrders;
		} catch (Exception exception) {

			logger.error(exception.getMessage());
			throw new AgroGeniusException(exception.getMessage());
		}

	}

	@Override
	public Order createNewOrder(String email, OrderDto orderDto) throws AgroGeniusException {

		try {

			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {

				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User not found");
			}

			if (orderDto == null) {
				logger.error("Empty Order data found !!");
				throw new AgroGeniusException("With Empty Order data cannot place an order!!");
			}

			Optional<Order> checkOrder = orderRepository.findById(orderDto.getId());

			if (checkOrder.isEmpty()) {
				logger.error("Order not found");
				throw new AgroGeniusException("Order not found");
			}

			Order order = checkOrder.get();

			// set Product
			Set<OrderProduct> orderProducts = new HashSet<>();

			for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {

				Optional<Product> product = productsRepository.findById(orderProductDto.getProductId());

				if (product.isEmpty()) {
					logger.error("Product doesn't exist");
					throw new AgroGeniusException("Product doesn't exist");
				}

				// check quantity
				if (product.get().getQuantity() < orderProductDto.getQuantity()) {

					logger.error("Not enough quantity");
					throw new AgroGeniusException("Not enough quantity");
				}

				OrderProduct orderProduct = OrderUtils.convertOrderProductDtoToEntity(orderProductDto);
				orderProduct.setOrder(order);

				// Update quantity
				Product updateProduct = product.get();
				updateProduct.setQuantity(product.get().getQuantity() - orderProductDto.getQuantity());
				productsRepository.saveAndFlush(updateProduct);

				orderProduct.setProduct(product.get());

				orderProducts.add(orderProduct);
			}

			orderProductRepository.saveAll(orderProducts);

			logger.info("{}'s Order created successfully.", user.get().getFullName());
			return order;

		} catch (Exception exception) {

			logger.error(exception.getMessage());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

	public String updateOrder(OrderDto orderDto) throws AgroGeniusException {
		try {
//			
//			Optional<Order> existingOrder = orderRepository.findById(orderId);
//			if (existingOrder.isEmpty()) {
//
//				logger.error("Order Not found");
//				throw new AgroGeniusException("Order Not found");
//			}

			// Update Order Status
			if (orderDto == null) {
				throw new AgroGeniusException("Empty Order found");
			}

			String razorPayOrderId = orderDto.getRazorPayOrderID();
			Order order = orderRepository.findByRazorPayOrderID(razorPayOrderId);
			if (order == null) {
				logger.error("Order not found for Razorpay Order ID: " + razorPayOrderId);
				throw new IllegalArgumentException("Order not found for Razorpay Order ID: " + razorPayOrderId);
			}

			order.setOrderStatus("PAYMENT_COMPLETED");
			Order updatedOrder = orderRepository.save(order);

			Set<OrderProduct> orderProducts = new HashSet<>();

			for (OrderProductDto orderProductDto : orderDto.getOrderProducts()) {

				Optional<Product> product = productsRepository.findById(orderProductDto.getProductId());

				if (product.isEmpty()) {
					logger.error("Product doesn't exist");
					throw new AgroGeniusException("Product doesn't exist");
				}

				// check quantity

				if (product.get().getQuantity() < orderProductDto.getQuantity()) {

					logger.error("Not enough quantity");
					throw new AgroGeniusException("Not enough quantity");
				}

				OrderProduct orderProduct = OrderUtils.convertOrderProductDtoToEntity(orderProductDto);
				orderProduct.setOrder(updatedOrder);

				// Update quantity
				Product updateProduct = product.get();
				updateProduct.setQuantity(product.get().getQuantity() - orderProductDto.getQuantity());
				productsRepository.saveAndFlush(updateProduct);

				orderProduct.setProduct(product.get());

				orderProducts.add(orderProduct);
			}

			orderProductRepository.saveAll(orderProducts);

			logger.info("Order Updated successfully");
			return "Order created successfully";
		} catch (Exception exception) {
			logger.error("Failed to Update Order : " + exception.getLocalizedMessage());
			throw new AgroGeniusException("Failed to Update Order : " + exception.getMessage());
		}
	}

	@Transactional
	@Override
	public String cancelOrderById(Integer orderId) throws AgroGeniusException {

		try {
			Optional<Order> order = orderRepository.findById(orderId);
			if (order.isEmpty()) {

				logger.error("Order Not found");
				throw new AgroGeniusException("Order Not found");
			}

			orderRepository.deleteById(orderId);

			logger.info("Order deleted successfully.");
			return "Order deleted successfully.";
		} catch (Exception exception) {

			logger.error(exception.getMessage());
			throw new AgroGeniusException(exception.getMessage());
		}
	}

	@Override
	public Order createOrderOnlyForPayment(String email, OrderDto orderDto) throws AgroGeniusException {
		try {

			Optional<User> user = userRepository.findByEmailId(email);
			if (user.isEmpty()) {

				logger.error(" User doesn't exist");
				throw new AgroGeniusException("User not found");
			}

			JSONObject orderReq = new JSONObject();

			orderReq.put("amount", orderDto.getAmount() * 100); // amount in paisa
			orderReq.put("currency", "INR");
			orderReq.put("receipt", user.get().getEmailId());

			this.client = new RazorpayClient(razorPayKey, razorPaySecret);

			// create order in razorpay
			com.razorpay.Order razorpayOrder = client.orders.create(orderReq);

			orderDto.setRazorPayOrderID(razorpayOrder.get("id"));
			orderDto.setOrderStatus(razorpayOrder.get("status"));

			Order order = OrderUtils.convertOrderDtoToEntity(orderDto);
			order.setCreatedAt(LocalDateTime.now());

			order.setUser(user.get());

			order = orderRepository.save(order);

			logger.info(user.get().getFullName() + " order created successfully");
			return order;

		} catch (Exception exception) {

			logger.error("Failed to create order : " + exception.getMessage());
			throw new AgroGeniusException("Failed to create order : " + exception.getMessage());
		}
	}

}
