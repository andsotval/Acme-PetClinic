package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductServiceTest {
	
	@Autowired
	protected ProductService productService;
	
	@Test
	public void  TestFindAllProductsByManagerIdPositive(){
		int providerId = 1;
		this.productService.findProductsAvailableByProviderId(providerId).forEach(product -> assertEquals(providerId, product.getProvider().getId()));
	}
	@Test
	public void  TestFindAllProductsByManagerIdNegative(){
		int providerId = 1;
		int notProviderId = 2;
		this.productService.findProductsAvailableByProviderId(notProviderId).forEach(product -> assertNotEquals(providerId, product.getProvider().getId()));
	}
	@Test
	public void  TestFindAllProductsByManagerIdNegativeNotPresent(){
		int providerId = 20;
		this.productService.findProductsAvailableByProviderId(providerId).forEach(o -> assertEquals(null, o.getProvider()));
	}

}
