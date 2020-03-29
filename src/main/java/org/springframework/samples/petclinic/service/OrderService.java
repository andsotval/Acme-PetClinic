package org.springframework.samples.petclinic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Transactional(readOnly = true)
	public Iterable<Order> findAllOrders(){
		return orderRepository.findAll();
	}

}
