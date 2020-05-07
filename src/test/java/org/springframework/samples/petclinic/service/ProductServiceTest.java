
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductServiceTest {

	private static final int	TEST_PRODUCT_ID	= 1;

	@Autowired
	protected ProductService	productService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindAllProductsByManagerIdPositive() {
		int providerId = 1;
		productService.findProductsAvailableByProviderId(providerId)
			.forEach(product -> assertEquals(providerId, product.getProvider().getId()));
	}
	@Test
	public void TestFindAllProductsByManagerIdNegative() {
		int providerId = 1;
		int notProviderId = 2;
		productService.findProductsAvailableByProviderId(notProviderId)
			.forEach(product -> assertNotEquals(providerId, product.getProvider().getId()));
	}
	@Test
	public void TestFindAllProductsByManagerIdNegativeNotPresent() {
		int providerId = 20;
		productService.findProductsAvailableByProviderId(providerId).forEach(o -> assertEquals(null, o.getProvider()));
	}

	@Test
	public void testSaveProduct() {
		//Fixture
		Product entity = new Product();
		entity.setId(ProductServiceTest.TEST_PRODUCT_ID);
		entity.setName("Comida para perros castrados");
		entity.setPrice(15.95);
		entity.setAvailable(true);

		//Act
		productService.saveEntity(entity);

		//Assert
		Assert.assertEquals("Comida para perros castrados",
			productService.findEntityById(ProductServiceTest.TEST_PRODUCT_ID).get().getName());
	}

	@Test
	public void testSaveProductWithoutName() {
		//Fixture
		Product entity = new Product();
		entity.setId(ProductServiceTest.TEST_PRODUCT_ID);
		entity.setPrice(15.95);
		entity.setAvailable(true);

		Validator validator = createValidator();
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		Iterator<ConstraintViolation<Product>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Product> violation = it.next();
			String message = violation.getMessage();
			System.out.println(message);

			switch (violation.getPropertyPath().toString()) {
			case "name":
				assertTrue(message.equals("no puede estar vacío"));
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteProduct() {
		Optional<Product> entity = productService.findEntityById(1);
		assertTrue(entity.isPresent());
		productService.deleteEntity(entity.get());

		Optional<Product> deleteEntity = productService.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteProductNonExisisting() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		assertEquals(collection.size(), 16);

		productService.deleteEntity(null);

		Collection<Product> collectionAfter = (Collection<Product>) productService.findAllEntities();
		assertEquals(collectionAfter.size(), 16);
	}

	@Test
	public void testDeleteProductById() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		assertEquals(collection.size(), 16);

		productService.deleteEntityById(1);

		Collection<Product> collectionAfter = (Collection<Product>) productService.findAllEntities();
		assertEquals(collectionAfter.size(), 16 - 1);
	}

	/*
	 * @Test
	 * public void testDeleteVisitByIdNonExisting() {
	 * Collection<Visit> collection = (Collection<Visit>) service.findAllEntities();
	 * assertEquals(collection.size(), 11);
	 * 
	 * service.deleteEntityById(90000);
	 * 
	 * Collection<Visit> collectionAfter = (Collection<Visit>) service.findAllEntities();
	 * assertEquals(collectionAfter.size(), 11);
	 * }
	 */

	@Test
	public void testFindAllProducts() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		assertEquals(collection.size(), 16);
	}

	@Test
	public void testFindProductById() {
		Optional<Product> entity = productService.findEntityById(1);
		assertTrue(entity.isPresent());
		assertEquals(entity.get().getId(), 1);
	}

	@Test
	public void testFindProductByIdNonExisiting() {
		Optional<Product> entity = productService.findEntityById(900000);
		assertFalse(entity.isPresent());
	}

}
