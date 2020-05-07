/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.ProductOrder;

public interface ProductOrderRepository extends BaseRepository<ProductOrder> {

	@Query("SELECT po FROM ProductOrder po WHERE po.order.id=?1 ORDER BY product.name")
	Collection<ProductOrder> findProductOrderByOrder(int orderId);

}
