
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.samples.petclinic.web.PetTypeController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PetTypeControllerIntegrationTests {

	private static final int	TEST_PETTYPE_ID_1	= 1;

	private static final int	TEST_PETTYPE_ID_7	= 7;

	@Autowired
	private PetTypeController	petTypeController;

	@Autowired
	private PetTypeService		petTypeService;


	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	public void TestListAvailable() {
		ModelMap model = new ModelMap();
		String view = petTypeController.listAvailable(model);

		assertEquals(view, "/pettype/list");

		Collection<PetType> list = petTypeService.findAvailable();

		assertNotNull(model.get("pettypes"));
		assertEquals(((Collection<PetType>) model.get("pettypes")).size(), list.size());
		((Collection<PetType>) model.get("pettypes")).forEach(pettype -> {
			list.contains(pettype);
		});

		assertNotNull(model.get("available"));
		assertEquals(model.get("available"), true);
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(2)
	public void TestListNotAvailable() {
		ModelMap model = new ModelMap();
		String view = petTypeController.listNotAvailable(model);

		assertEquals(view, "/pettype/list");

		Collection<PetType> list = petTypeService.findNotAvailable();

		assertNotNull(model.get("pettypes"));
		assertEquals(((Collection<PetType>) model.get("pettypes")).size(), list.size());
		((Collection<PetType>) model.get("pettypes")).forEach(pettype -> {
			list.contains(pettype);
		});

		assertNotNull(model.get("available"));
		assertEquals(model.get("available"), false);
	}

	@Test
	@Order(3)
	public void TestInitCreationForm() {
		ModelMap model = new ModelMap();
		String view = petTypeController.initCreationForm(model);

		assertEquals(view, "pettype/createOrUpdatePettypeForm");

		assertNotNull(model.get("petType"));
		assertEquals(((PetType) model.get("petType")).getAvailable(), true);
	}

	@Test
	@Order(4)
	public void TestProcessCreationFormPositive() {
		ModelMap model = new ModelMap();

		PetType petType = new PetType();
		petType.setAvailable(true);
		petType.setName("PetType 1");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = petTypeController.processCreationForm(petType, bindingResult, model);

		assertEquals(view, "redirect:/pettype/listAvailable");

		List<PetType> petTypes = petTypeService.findAvailable().stream().filter(pt -> pt.getName().equals("PetType 1"))
			.collect(Collectors.toList());

		assertNotNull(petTypes.get(0));
	}

	@Test
	@Order(5)
	public void TestProcessCreationFormNegative() {
		ModelMap model = new ModelMap();

		PetType petType = new PetType();
		petType.setAvailable(true);
		petType.setName("");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "Requied!");
		String view = petTypeController.processCreationForm(petType, bindingResult, model);

		assertEquals(view, "pettype/createOrUpdatePettypeForm");

		assertNotNull(model.get("petType"));
	}

	@Test
	@Order(6)
	public void TestInitUpdateForm() {
		ModelMap model = new ModelMap();
		String view = petTypeController.initUpdateForm(TEST_PETTYPE_ID_1, model);

		assertEquals(view, "pettype/createOrUpdatePettypeForm");

		PetType petType = petTypeService.findEntityById(TEST_PETTYPE_ID_1).get();
		assertNotNull(model.get("petType"));
		assertEquals(((PetType) model.get("petType")).getId(), petType.getId());
		assertEquals(((PetType) model.get("petType")).getName(), petType.getName());
		assertEquals(((PetType) model.get("petType")).getAvailable(), petType.getAvailable());
	}

	@Test
	@Order(7)
	public void TestProcessUpdateFormPositive() {
		ModelMap model = new ModelMap();

		PetType petType = petTypeService.findEntityById(TEST_PETTYPE_ID_1).get();
		petType.setName("PetType 1 update");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = petTypeController.processUpdateForm(TEST_PETTYPE_ID_1, petType, bindingResult, model);

		List<PetType> petTypes = petTypeService.findAvailable().stream()
			.filter(pt -> pt.getName().equals("PetType 1 update")).collect(Collectors.toList());

		assertNotNull(petTypes.get(0));
		assertEquals(petTypes.get(0).getName(), "PetType 1 update");

		assertEquals(view, "redirect:/pettype/listAvailable");
	}

	@Test
	@Order(8)
	public void TestProcessUpdateFormNegative() {
		ModelMap model = new ModelMap();

		PetType petType = petTypeService.findEntityById(TEST_PETTYPE_ID_1).get();
		petType.setName("");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "Requied!");
		String view = petTypeController.processUpdateForm(TEST_PETTYPE_ID_1, petType, bindingResult, model);

		assertEquals(view, "pettype/createOrUpdatePettypeForm");

		assertNotNull(model.get("petType"));
		assertEquals(((PetType) model.get("petType")).getId(), petType.getId());
		assertEquals(((PetType) model.get("petType")).getName(), petType.getName());
		assertEquals(((PetType) model.get("petType")).getAvailable(), petType.getAvailable());
	}

	@Test
	@Order(9)
	public void TestAvailable() {
		ModelMap model = new ModelMap();
		String view = petTypeController.available(TEST_PETTYPE_ID_7, model);

		assertEquals(view, "redirect:/pettype/listAvailable");

		PetType petType = petTypeService.findEntityById(TEST_PETTYPE_ID_7).get();
		assertEquals(petType.getAvailable(), true);
	}

	@Test
	@Order(10)
	public void TestNotAvailable() {
		ModelMap model = new ModelMap();
		String view = petTypeController.notAvailable(TEST_PETTYPE_ID_1, model);

		assertEquals(view, "redirect:/pettype/listNotAvailable");

		PetType petType = petTypeService.findEntityById(TEST_PETTYPE_ID_1).get();
		assertEquals(petType.getAvailable(), false);
	}

}