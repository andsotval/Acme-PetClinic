/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Product;

public interface ProductRepository extends NamedRepository<Product> {

	@Query("SELECT product FROM Product product WHERE (product.provider.id=?1) AND	(product.available = true) ORDER BY product.name")
	Collection<Product> findProductsAvailableByProviderId(int providerId);

	@Query("SELECT product FROM Product product WHERE (product.provider.id=?1) ORDER BY product.name")
	Collection<Product> findAllProductsByProviderId(int providerId);

}
