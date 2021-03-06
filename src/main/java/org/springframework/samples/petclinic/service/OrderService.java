/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService extends BaseService<Order> {

	private OrderRepository orderRepository;


	@Autowired
	public OrderService(BaseRepository<Order> repository, OrderRepository orderRepository) {
		super(repository);
		this.orderRepository = orderRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Order> findAllOrdersByManagerId(final int managerId) {
		return orderRepository.findAllOrdersByManagerId(managerId);
	}

	@Transactional(readOnly = true)
	public Collection<Order> findOrdersByProviderId(final int providerId) {
		return orderRepository.findOrdersByProviderId(providerId);
	}

}
