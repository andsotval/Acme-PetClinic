/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ActiveProfiles("hsqldb")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class OrderServiceTests {

	@Autowired
	protected OrderService			orderService;
	@Autowired
	protected ProductOrderService	productOrderService;

	private int						TEST_MANAGER_ID					= 1;

	private int						TEST_MANAGER_ID_NOT_PRESENT		= 100;

	private int						TEST_PROVIDER_ID				= 1;

	private int						TEST_PROVIDER_ID_NOT_PRESENT	= 100;

	private int						TEST_ORDER_ID					= 1;

	private int						TEST_ORDER_ID_NOT_PRESENT		= 100;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindAllOrdersByManagerId() {
		Collection<Order> orders = orderService.findAllOrdersByManagerId(TEST_MANAGER_ID);
		orders.forEach(o -> assertEquals(TEST_MANAGER_ID, o.getManager().getId()));
	}

	@Test
	public void TestFindAllOrdersByManagerIdNotPresent() {
		Collection<Order> orders = orderService.findAllOrdersByManagerId(TEST_MANAGER_ID_NOT_PRESENT);
		assertEquals(0, orders.size());
	}

	@Test
	public void TestFindAllOrdersByProviderId() {
		Collection<Order> orders = orderService.findOrdersByProviderId(TEST_PROVIDER_ID);
		orders.forEach(o -> assertTrue(productOrderService.findProviderByOrder(o.getId()).getId() == TEST_PROVIDER_ID));
	}

	@Test
	public void TestFindOrdersByProviderIdNotPresent() {
		Collection<Order> orders = orderService.findOrdersByProviderId(TEST_PROVIDER_ID_NOT_PRESENT);
		assertEquals(0, orders.size());
	}

	@Test
	public void testFindAllOrders() {
		Collection<Order> collection = (Collection<Order>) orderService.findAllEntities();
		assertEquals(collection.size(), 6);
	}

	@Test
	public void testFindOrderById() {
		Optional<Order> entity = orderService.findEntityById(TEST_ORDER_ID);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(TEST_ORDER_ID));
	}

	@Test
	public void testFindOrderByIdNotPresent() {
		Optional<Order> entity = orderService.findEntityById(TEST_ORDER_ID_NOT_PRESENT);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveOrder() {
		Collection<Order> collection = (Collection<Order>) orderService.findAllEntities();
		int collectionSize = collection.size();

		LocalDate date = LocalDate.now();
		Order order = new Order();
		order.setDate(date);
		order.setIsAccepted(false);

		Manager manager = new Manager();
		manager.setId(1);

		order.setManager(manager);
		orderService.saveEntity(order);

		collection = (Collection<Order>) orderService.findAllEntities();
		assertEquals(collection.size(), collectionSize + 1);

		Optional<Order> newEntity = orderService.findEntityById(collectionSize + 1);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getDate(), date);
		assertEquals(newEntity.get().getIsAccepted(), false);
		assertEquals(newEntity.get().getManager(), manager);
	}

	@Test
	public void testSaveOrderWithNullParameters() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Order order = new Order();
		order.setDate(null);
		order.setIsAccepted(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);
		assertEquals(constraintViolations.size(), 1);

		Iterator<ConstraintViolation<Order>> it = constraintViolations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<Order> violation = it.next();
			String message = violation.getMessage();

			switch (violation.getPropertyPath().toString()) {
			case "date":
				assertEquals(message, "must not be null");
				break;
			case "isAccepted":
				assertEquals(message, "must not be null");
				break;
			default:
				break;
			}
		}
	}

	@Test
	public void testDeleteOrder() {
		Optional<Order> entity = orderService.findEntityById(TEST_ORDER_ID);
		assertTrue(entity.isPresent());
		orderService.deleteEntity(entity.get());

		Optional<Order> deleteEntity = orderService.findEntityById(TEST_ORDER_ID);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteOrderNotPresent() {
		Collection<Order> collection = (Collection<Order>) orderService.findAllEntities();
		int collectionSize = collection.size();

		orderService.deleteEntity(null);

		Collection<Order> newCollection = (Collection<Order>) orderService.findAllEntities();
		int newCollectionSize = newCollection.size();

		assertEquals(collectionSize, newCollectionSize);
	}

	@Test
	public void testDeleteOrderById() {
		orderService.deleteEntityById(TEST_ORDER_ID);

		Optional<Order> entity = orderService.findEntityById(TEST_ORDER_ID);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testDeleteOrderByIdNotPresent() {
		boolean deleted = true;

		try {
			orderService.deleteEntityById(TEST_ORDER_ID_NOT_PRESENT);
		} catch (EmptyResultDataAccessException e) {
			deleted = false;
		}

		assertFalse(deleted);
	}
}
