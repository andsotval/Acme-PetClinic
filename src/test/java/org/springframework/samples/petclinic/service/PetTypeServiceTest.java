
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PetTypeServiceTest {

	@Autowired
	protected PetTypeService service;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	public void testFindAvailable() {
		Collection<PetType> collection = service.findAvailable();
		assertEquals(collection.size(), 6);

		collection.forEach(pettype -> assertEquals(pettype.getAvailable(), true));
	}

	@Test
	public void testFindNotAvailable() {
		Collection<PetType> collection = service.findNotAvailable();
		assertEquals(collection.size(), 1);

		collection.forEach(pettype -> assertEquals(pettype.getAvailable(), false));
	}

	@Test
	public void testFindAllEntities() {
		Collection<PetType> collection = (Collection<PetType>) service.findAllEntities();
		assertEquals(collection.size(), 7);
	}

	@Test
	public void testFindEntityById() {
		Optional<PetType> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
	}

	@Test
	public void testSaveEntityPositive() {
		Collection<PetType> collection = (Collection<PetType>) service.findAllEntities();
		assertEquals(collection.size(), 7);

		PetType entity = new PetType();
		entity.setAvailable(true);
		entity.setName("name1");
		service.saveEntity(entity);

		collection = (Collection<PetType>) service.findAllEntities();
		assertEquals(collection.size(), 8);

		Optional<PetType> newEntity = service.findEntityById(8);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getAvailable(), true);
		assertEquals(newEntity.get().getName(), "name1");
	}

	@Test
	public void testSaveEntityNegative() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		PetType entity = new PetType();
		entity.setName("name1");

		Validator validator = createValidator();
		Set<ConstraintViolation<PetType>> constraintViolations = validator.validate(entity);
		assertEquals(constraintViolations.size(), 1);

		ConstraintViolation<PetType> violation = constraintViolations.iterator().next();
		assertEquals(violation.getPropertyPath().toString(), "available");
		assertEquals(violation.getMessage(), "must not be null");

	}

	@Test
	public void testDeleteEntity() {
		Optional<PetType> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
		service.deleteEntity(entity.get());

		Optional<PetType> deleteEntity = service.findEntityById(1);
		assertTrue(!deleteEntity.isPresent());
	}

	@Test
	public void testDeleteEntityById() {
		service.deleteEntityById(1);

		Optional<PetType> entity = service.findEntityById(1);
		assertTrue(!entity.isPresent());
	}

}
