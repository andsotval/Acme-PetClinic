
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductServiceTest {

	private int					TEST_PRODUCT_ID					= 1;

	private int					TEST_PRODUCT_ID_NOT_PRESENT		= 100;

	private int					TEST_PROVIDER_ID				= 1;

	private int					TEST_PROVIDER_ID_NOT_PRESENT	= 100;

	@Autowired
	protected ProductService	productService;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindProductsByProviderId() {
		Collection<Product> products = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID);
		products.forEach(product -> assertEquals(TEST_PROVIDER_ID, product.getProvider().getId()));
	}

	@Test
	public void testFindProductsByProviderIdNotPresent() {
		Collection<Product> products = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID_NOT_PRESENT);
		assertEquals(products.size(), 0);
	}

	@Test
	public void testFindAllProducts() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		assertEquals(collection.size(), 16);
	}

	@Test
	public void testFindProductById() {
		Optional<Product> entity = productService.findEntityById(TEST_PRODUCT_ID);
		assertTrue(entity.isPresent());
		assertEquals(entity.get().getId(), TEST_PRODUCT_ID);
	}

	@Test
	public void testFindProductByIdNotPresent() {
		Optional<Product> entity = productService.findEntityById(TEST_PRODUCT_ID_NOT_PRESENT);
		assertFalse(entity.isPresent());
	}

	@Test
	public void testSaveProduct() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		int collectionSize = collection.size();

		Product entity = new Product();
		entity.setName("Comida para perros castrados");
		entity.setPrice(15.95);
		entity.setAvailable(true);
		entity.setTax(1.20);

		productService.saveEntity(entity);

		Collection<Product> newCollection = (Collection<Product>) productService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize + 1, newCollectionSize);

		Assert.assertEquals("Comida para perros castrados",
			productService.findEntityById(newCollectionSize).get().getName());
	}

	@Test
	public void testSaveProductWithoutName() {
		Product entity = new Product();
		entity.setId(TEST_PRODUCT_ID);
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
		Optional<Product> entity = productService.findEntityById(TEST_PRODUCT_ID);
		assertTrue(entity.isPresent());
		productService.deleteEntity(entity.get());

		Optional<Product> deleteEntity = productService.findEntityById(TEST_PRODUCT_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteProductNotPresent() {
		Collection<Product> collection = (Collection<Product>) productService.findAllEntities();
		int collectionSize = collection.size();

		productService.deleteEntity(null);

		Collection<Product> newCollection = (Collection<Product>) productService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteProductById() {
		productService.deleteEntityById(TEST_PRODUCT_ID);

		Optional<Product> entity = productService.findEntityById(TEST_PRODUCT_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteProductByIdNotPresent() {
		boolean deleted = true;

		try {
			productService.deleteEntityById(TEST_PRODUCT_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
