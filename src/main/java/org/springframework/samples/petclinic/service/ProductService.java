/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

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
	public Collection<Product> findProductsAvailableByProviderId(int providerId) {
		return productRepository.findProductsAvailableByProviderId(providerId);
	}

	@Transactional(readOnly = true)
	public Collection<Product> findProductsByIds(Collection<Integer> ids) {
		return (Collection<Product>) productRepository.findAllById(ids);
	}

}
