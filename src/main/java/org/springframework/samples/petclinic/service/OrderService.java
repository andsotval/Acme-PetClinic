package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	
	private OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository =orderRepository;
	}
	
	@Transactional(readOnly = true)
	public Iterable<Order> findAllOrders(){
		return this.orderRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Iterable<Order> findAllOrdersByManagerId(final int managerId){
		return this.orderRepository.findAllOrdersByManagerId(managerId);
	}
	
	@Transactional(readOnly = true)
	public Optional<Order> findOrderById(final int orderId){
		return this.orderRepository.findById(orderId);
	}
	
	public void saveOrder(final Order order) {
		this.orderRepository.save(order);
	}
	

}
