package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer>{
	
	@Query("SELECT order FROM Order order WHERE order.manager.id=?1")
	Iterable<Order> findAllOrdersByManagerId(int managerId);
	
	

}
