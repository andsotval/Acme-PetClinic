
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class OrderServiceTests {

	@Autowired
	protected OrderService service;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindAllOrdersByManagerIdPositive() {
		int managerId = 1;
		service.findAllOrdersByManagerId(managerId).forEach(o -> assertEquals(managerId, o.getManager().getId()));
	}

	@Test
	public void TestFindAllOrdersByManagerIdNegative() {
		int managerId = 1;
		int notManagerId = 2;
		service.findAllOrdersByManagerId(notManagerId)
			.forEach(order -> assertNotEquals(managerId, order.getManager().getId()));
	}
	@Test
	public void TestFindAllOrdersByManagerIdNegativeNotPresent() {
		int managerId = 10;
		service.findAllOrdersByManagerId(managerId).forEach(o -> assertEquals(null, o.getManager()));
	}

	@Test
	public void testFindAllEntities() {
		Collection<Order> collection = (Collection<Order>) service.findAllEntities();
		assertEquals(collection.size(), 6);
	}

	@Test
	public void testFindEntityByIdPositive() {
		Optional<Order> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<Order> entity = service.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<Order> collection = (Collection<Order>) service.findAllEntities();
		assertEquals(collection.size(), 6);

		LocalDate date = LocalDate.now();
		Order order = new Order();
		order.setDate(date);
		order.setIsAccepted(false);

		Manager manager = new Manager();
		manager.setId(1);

		order.setManager(manager);
		service.saveEntity(order);

		collection = (Collection<Order>) service.findAllEntities();
		assertEquals(collection.size(), 7);

		Optional<Order> newEntity = service.findEntityById(7);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getDate(), date);
		assertEquals(newEntity.get().getIsAccepted(), false);
		assertEquals(newEntity.get().getManager(), manager);
	}

	@Test
	public void testSaveEntityNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Order order = new Order();
		order.setDate(null);
		order.setIsAccepted(null);

		Validator validator = createValidator();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);
		assertEquals(constraintViolations.size(), 2);

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
	public void testDeleteEntity() {
		Optional<Order> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		service.deleteEntity(entity.get());

		Optional<Order> deleteEntity = service.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		service.deleteEntityById(1);

		Optional<Order> entity = service.findEntityById(1);
		assertTrue(!entity.isPresent());
	}
}
