package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{

	@Query("SELECT product FROM Product product WHERE (product.provider.manager.id=?1) AND	(product.available = true) ORDER BY product.provider.id")
	Collection<Product> findProductsAvailableForManager(int managerId);
	
}
