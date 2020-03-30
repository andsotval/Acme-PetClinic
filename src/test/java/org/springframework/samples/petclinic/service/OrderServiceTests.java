package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class OrderServiceTests {
	
	@Autowired
	protected OrderService orderService;
	
	@Test
	public void  TestFindAllOrdersByManagerIdPositive(){
		int managerId = 1;
		this.orderService.findAllOrdersByManagerId(managerId).forEach(o -> assertEquals(managerId, o.getManager().getId()));
	}
	@Test
	public void  TestFindAllOrdersByManagerIdNegative(){
		int managerId = 1;
		int notManagerId = 2;
		this.orderService.findAllOrdersByManagerId(notManagerId).forEach(order -> assertNotEquals(managerId, order.getManager().getId()));
	}
	@Test
	public void  TestFindAllOrdersByManagerIdNegativeNotPresent(){
		int managerId = 10;
		this.orderService.findAllOrdersByManagerId(managerId).forEach(o -> assertEquals(null, o.getManager()));
	}
	

}
