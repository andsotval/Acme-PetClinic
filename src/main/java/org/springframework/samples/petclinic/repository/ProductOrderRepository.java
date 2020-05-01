
package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends BaseRepository<ProductOrder> {

	@Query("SELECT po FROM ProductOrder po WHERE po.order.id=?1 ORDER BY product.name")
	Iterable<ProductOrder> findProductOrderByOrder(int orderId);

}
