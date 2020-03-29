package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Transactional(readOnly = true)
	public Iterable<Product> findAllOrders(){
		return this.productRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Iterable<Product> findProductsAvailableForManager(int managerId){
		return this.productRepository.findProductsAvailableForManager(managerId);
	}
	
	@Transactional
	public void saveOrder(final Product product) {
		this.productRepository.save(product);
	}
	

}
