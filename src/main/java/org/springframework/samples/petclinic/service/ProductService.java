
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService extends BaseService<Product> {

	private ProductRepository productRepository;


	@Autowired
	public ProductService(BaseRepository<Product> repository, ProductRepository productRepository) {
		super(repository);
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Product> findProductsAvailableByProviderId(int providerId) {
		return productRepository.findProductsAvailableByProviderId(providerId);
	}

}
