
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order> {

	@Query("SELECT order FROM Order order WHERE order.manager.id=?1 ORDER BY order.isAccepted desc, order.date desc")
	Collection<Order> findAllOrdersByManagerId(int managerId);

}
