
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductOrderServiceTests {

	@Autowired
	protected ProductOrderService service;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void TestFindProductOrderByOrderdPositive() {
		Collection<ProductOrder> collection = service.findProductOrderByOrder(1);
		assertEquals(collection.size(), 3);
	}

	@Test
	public void TestfindProviderByOrderPositive() {
		Provider provider = service.findProviderByOrder(1);
		assertEquals(provider.getId(), 1);
	}

	@Test
	public void testFindAllEntities() {
		Collection<ProductOrder> collection = (Collection<ProductOrder>) service.findAllEntities();
		assertEquals(collection.size(), 12);
	}

	@Test
	public void testFindEntityByIdPositive() {
		Optional<ProductOrder> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		assertTrue(entity.get().getId().equals(1));
	}

	@Test
	public void testFindEntityByIdNegative() {
		Optional<ProductOrder> entity = service.findEntityById(99);
		assertTrue(!entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<ProductOrder> collection = (Collection<ProductOrder>) service.findAllEntities();
		assertEquals(collection.size(), 12);

		ProductOrder productOrder = new ProductOrder();
		productOrder.setName("name1");
		productOrder.setAmount(3);
		productOrder.setPrice(10.0);
		productOrder.setTax(12.0);

		Order order = new Order();
		order.setId(1);
		productOrder.setOrder(order);

		Product product = new Product();
		product.setId(1);
		productOrder.setProduct(product);

		service.saveEntity(productOrder);

		collection = (Collection<ProductOrder>) service.findAllEntities();
		assertEquals(collection.size(), 13);

		Optional<ProductOrder> newEntity = service.findEntityById(13);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getName(), "name1");
		assertEquals(newEntity.get().getAmount(), 3);
		assertEquals(newEntity.get().getPrice(), 10.0);
		assertEquals(newEntity.get().getTax(), 12.0);
		assertEquals(newEntity.get().getOrder(), order);
		assertEquals(newEntity.get().getProduct(), product);
	}

	@Test
	public void testSaveEntityNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		ProductOrder productOrder = new ProductOrder();
		productOrder.setName("name1");
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
	public void testDeleteEntity() {
		Optional<ProductOrder> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		service.deleteEntity(entity.get());

		Optional<ProductOrder> deleteEntity = service.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		service.deleteEntityById(1);

		Optional<ProductOrder> entity = service.findEntityById(1);
		assertTrue(!entity.isPresent());
	}

}
