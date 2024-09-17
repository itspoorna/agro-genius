package com.bvb.agroGenius.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bvb.agroGenius.dto.OrderDto;
import com.bvb.agroGenius.exception.AgroGeniusException;
import com.bvb.agroGenius.models.Order;
import com.bvb.agroGenius.rest.AgroGeniusResponse;
import com.bvb.agroGenius.service.OrderServices;

@RestController
@RequestMapping("/order")
public class OrderController {

	Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderServices orderService;

	@GetMapping("/user/{email}")
	public AgroGeniusResponse getAllOrdersById(@PathVariable(name = "email") String email) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			List<OrderDto> listOfDto = orderService.getOrdersById(email);
			status = HttpStatus.OK;
			if (!listOfDto.isEmpty()) {
				return new AgroGeniusResponse(listOfDto, status);
			}
			message = "Empty List";
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Order data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}
	
	@PostMapping("/create-order/{email}")
	public AgroGeniusResponse createOrderOnlyForPayment(@PathVariable(name = "email")String email, @RequestBody OrderDto orderDto){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			Order order = orderService.createOrderOnlyForPayment(email, orderDto);
			status = HttpStatus.OK;
			return new AgroGeniusResponse(order, status);
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@PostMapping("/user/{email}")
	public AgroGeniusResponse createNewOrder(@PathVariable(name = "email") String email,
			@RequestBody OrderDto orderDto) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			Order order = orderService.createNewOrder(email, orderDto);
			status = HttpStatus.OK;
			return new AgroGeniusResponse(order, status);
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@PostMapping("/handle-payment-callback")
	public AgroGeniusResponse handlePaymentCallback(@RequestBody OrderDto orderDto) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			//logger.info("Received Payment Callback Payload: " + respPayload);
			message = orderService.updateOrder(orderDto);
			status = HttpStatus.OK;
		}catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}
		return new AgroGeniusResponse(message, status);
	}

	@DeleteMapping("/{orderId}")
	public AgroGeniusResponse cancelOrderById(@PathVariable(name = "orderId") Integer orderId) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		String message = "";
		try {
			message = orderService.cancelOrderById(orderId);
			status = HttpStatus.OK;
		} catch (AgroGeniusException exception) {
			message = "Failed to retrieve Users data !!" + exception.getLocalizedMessage();
		}

		return new AgroGeniusResponse(message, status);
	}
}
