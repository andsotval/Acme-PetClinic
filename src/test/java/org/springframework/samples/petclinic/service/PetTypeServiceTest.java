
package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PetTypeServiceTest {

	@Autowired
	protected PetTypeService service;


	@Test
	public void testFindAvailable() {
		Collection<PetType> collection = service.findAvailable();
		assertEquals(collection.size(), 6);
	}

	@Test
	public void testFindAllEntities() {
		Collection<PetType> collection = (Collection<PetType>) service.findAllEntities();
		assertEquals(collection.size(), 6);
	}

	@Test
	public void testFindEntityById() {
		Optional<PetType> entity = service.findEntityById(1);
		assertTrue(entity.isPresent());
	}

	@Test
	public void testSaveEntity() {
		PetType entity = new PetType();
		entity.setAvailable(true);
		entity.setName("name1");
		service.saveEntity(entity);

		// Al no tener método para buscar la entidad por ninguna propiedad
		// buscamos la entidad por el identificador creado (1 + MAX(identificador en BD)
		Optional<PetType> newEntity = service.findEntityById(7);
		assertTrue(newEntity.isPresent());
		assertEquals(newEntity.get().getAvailable(), true);
		assertEquals(newEntity.get().getName(), "name1");
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
