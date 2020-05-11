
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductOrderServiceTests {

	@Autowired
	protected ProductOrderService	productOrderService;

	@Autowired
	protected OrderService			orderService;

	private int						TEST_ORDER_ID						= 1;

	private int						TEST_ORDER_ID_NOT_PRESENT			= 100;

	private int						TEST_PRODUCT_ORDER_ID				= 1;

	private int						TEST_PRODUCT_ORDER_ID_NOT_PRESENT	= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindProductOrderByOrderId() {
		Collection<ProductOrder> productOrders = productOrderService.findProductOrderByOrder(TEST_ORDER_ID);
		productOrders.forEach(p -> assertEquals(p.getOrder().getId(), TEST_ORDER_ID));
	}

	@Test
	public void testFindProductOrderByOrderIdNotPresent() {
		Collection<ProductOrder> productOrders = productOrderService.findProductOrderByOrder(TEST_ORDER_ID_NOT_PRESENT);
		assertEquals(productOrders.size(), 0);
	}

	@Test
	public void testFindProviderByOrderId() {
		Provider provider = productOrderService.findProviderByOrder(TEST_ORDER_ID);
		Integer providerId = provider.getId();
		Collection<ProductOrder> productOrders = productOrderService.findProductOrderByOrder(TEST_ORDER_ID);
		productOrders.forEach(p -> assertEquals(p.getProduct().getProvider().getId(), providerId));
	}

	@Test
	public void testFindProviderByOrderIdNotPresent() {
		Provider provider = null;
		try {
			provider = productOrderService.findProviderByOrder(TEST_ORDER_ID_NOT_PRESENT);
		} catch (NoResultException e) {
			provider = null;
		}
		assertNull(provider);
	}

	@Test
	public void testFindAllProductOrders() {
		Collection<ProductOrder> collection = (Collection<ProductOrder>) productOrderService.findAllEntities();
		assertEquals(collection.size(), 12);
	}

	@Test
	public void testFindProductOrderById() {
		Optional<ProductOrder> entity = productOrderService.findEntityById(TEST_PRODUCT_ORDER_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_PRODUCT_ORDER_ID));
	}

	@Test
	public void testFindProductOrderByIdNotPresent() {
		Optional<ProductOrder> entity = productOrderService.findEntityById(TEST_PRODUCT_ORDER_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveProductOrder() {
		Collection<ProductOrder> collection = (Collection<ProductOrder>) productOrderService.findAllEntities();
		int collectionSize = collection.size();

		ProductOrder productOrder = new ProductOrder();
		productOrder.setAmount(3);
		productOrder.setPrice(10.0);
		productOrder.setTax(12.0);

		Order order = new Order();
		order.setId(1);
		productOrder.setOrder(order);

		Product product = new Product();
		product.setId(1);
		productOrder.setProduct(product);

		productOrderService.saveEntity(productOrder);

		collection = (Collection<ProductOrder>) productOrderService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<ProductOrder> newEntity = productOrderService.findEntityById(13);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getAmount(), 3);
		assertEquals(newEntity.get().getPrice(), 10.0);
		assertEquals(newEntity.get().getTax(), 12.0);
		assertEquals(newEntity.get().getOrder(), order);
		assertEquals(newEntity.get().getProduct(), product);
	}

	@Test
	public void testSaveProductOrderWithNullParameters() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		ProductOrder productOrder = new ProductOrder();
		productOrder.setAmount(null);
		productOrder.setPrice(null);
		productOrder.setTax(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<ProductOrder>> constraintViolations = validator.validate(productOrder);
		assertEquals(constraintViolations.size(), 3);

		Iterator<ConstraintViolation<ProductOrder>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<ProductOrder> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "price":
				assertEquals(message, "must not be null");
				break;
			case "tax":
				assertEquals(message, "must not be null");
				break;
			case "amount":
				assertEquals(message, "must not be null");
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteProductOrder() {
		Optional<ProductOrder> entity = productOrderService.findEntityById(TEST_PRODUCT_ORDER_ID);
		assertTrue(entity.isPresent());
		productOrderService.deleteEntity(entity.get());

		Optional<ProductOrder> deleteEntity = productOrderService.findEntityById(TEST_PRODUCT_ORDER_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteProductOrderNotPresent() {
		Collection<ProductOrder> collection = (Collection<ProductOrder>) productOrderService.findAllEntities();
		int collectionSize = collection.size();

		productOrderService.deleteEntity(null);

		Collection<ProductOrder> newCollection = (Collection<ProductOrder>) productOrderService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteProductOrderById() {
		productOrderService.deleteEntityById(TEST_PRODUCT_ORDER_ID);

		Optional<ProductOrder> entity = productOrderService.findEntityById(TEST_PRODUCT_ORDER_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteProductOrderByIdNotPresent() {
		boolean deleted = true;

		try {
			productOrderService.deleteEntityById(TEST_PRODUCT_ORDER_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}

}
