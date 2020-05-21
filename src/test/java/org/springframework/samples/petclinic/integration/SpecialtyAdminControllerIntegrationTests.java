/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.web.SpecialtyAdminController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SpecialtyAdminControllerIntegrationTests {

	private static final int			TEST_SPECIALTY_ID_1					= 1;

	private static final int			TEST_SPECIALTY_ID_2					= 2;

	private static final int			TEST_SPECIALTY_ID_99				= 99;

	private static final String			VIEW_REDIRECT_OUPS					= "redirect:/oups";

	private static final String			VIEW_SPECIALTY_ADMIN_LIST			= "/specialty/admin/list";

	private static final String			VIEW_SPECIALTY_ADMIN_CREATEORUPDATE	= "specialty/admin/createOrUpdateSpecialtyForm";

	@Autowired
	private SpecialtyAdminController	specialtyAdminController;

	@Autowired
	private SpecialtyService			specialtyService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(1)
	public void TestListAvailable() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.listAvailable(model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);

		Collection<Specialty> list = specialtyService.findAvailable();
		assertNotNull(model.get("specialties"));
		assertEquals(((Collection<Specialty>) model.get("specialties")).size(), list.size());
		((Collection<Specialty>) model.get("specialties")).forEach(specialty -> {
			list.contains(specialty);
		});
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(2)
	public void TestListNotAvailable() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.listNotAvailable(model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);

		Collection<Specialty> list = specialtyService.findNotAvailable();
		assertNotNull(model.get("specialties"));
		assertEquals(((Collection<Specialty>) model.get("specialties")).size(), list.size());
		((Collection<Specialty>) model.get("specialties")).forEach(specialty -> {
			list.contains(specialty);
		});
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(3)
	public void TestInitCreationForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.initCreationForm(model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_CREATEORUPDATE);

		assertNotNull(model.get("specialty"));
		assertEquals(((Specialty) model.get("specialty")).getAvailable(), true);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(4)
	public void TestProcessCreationFormPositive() {
		ModelMap model = new ModelMap();

		Specialty specialty = new Specialty();
		specialty.setAvailable(true);
		specialty.setName("Specialty 1");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = specialtyAdminController.processCreationForm(specialty, bindingResult, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);

		List<Specialty> specialties = specialtyService.findAvailable().stream()
			.filter(sp -> sp.getName().equals("Specialty 1")).collect(Collectors.toList());

		assertNotNull(specialties.get(0));
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(5)
	public void TestProcessCreationFormNegativeNameIsEmpty() {
		ModelMap model = new ModelMap();

		Specialty specialty = new Specialty();
		specialty.setAvailable(true);
		specialty.setName("");
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "Requied!");
		String view = specialtyAdminController.processCreationForm(specialty, bindingResult, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_CREATEORUPDATE);

		assertNotNull(model.get("specialty"));
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(6)
	public void TestInitUpdateForm() {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.initUpdateForm(TEST_SPECIALTY_ID_1, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_CREATEORUPDATE);

		Specialty specialty = specialtyService.findEntityById(TEST_SPECIALTY_ID_1).get();
		assertNotNull(model.get("specialty"));
		assertEquals(((Specialty) model.get("specialty")).getId(), specialty.getId());
		assertEquals(((Specialty) model.get("specialty")).getName(), specialty.getName());
		assertEquals(((Specialty) model.get("specialty")).getAvailable(), specialty.getAvailable());
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(7)
	public void TestInitUpdateFormValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.initUpdateForm(TEST_SPECIALTY_ID_99, model);

		assertEquals(view, VIEW_REDIRECT_OUPS);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(8)
	public void TestProcessUpdateFormPositive() {
		ModelMap model = new ModelMap();

		Specialty specialty = specialtyService.findEntityById(TEST_SPECIALTY_ID_1).get();
		specialty.setName("Specialty 1 update");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = specialtyAdminController.processUpdateForm(TEST_SPECIALTY_ID_1, specialty, bindingResult, model);

		List<Specialty> specialties = specialtyService.findAvailable().stream()
			.filter(sp -> sp.getName().equals("Specialty 1 update")).collect(Collectors.toList());

		assertNotNull(specialties.get(0));
		assertEquals(specialties.get(0).getName(), "Specialty 1 update");

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(9)
	public void TestProcessUpdateFormNegativeNameIsEmpty() {
		ModelMap model = new ModelMap();

		Specialty specialty = specialtyService.findEntityById(TEST_SPECIALTY_ID_1).get();
		specialty.setName("");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "Requied!");
		String view = specialtyAdminController.processUpdateForm(TEST_SPECIALTY_ID_1, specialty, bindingResult, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_CREATEORUPDATE);

		assertNotNull(model.get("specialty"));
		assertEquals(((Specialty) model.get("specialty")).getId(), specialty.getId());
		assertEquals(((Specialty) model.get("specialty")).getName(), specialty.getName());
		assertEquals(((Specialty) model.get("specialty")).getAvailable(), specialty.getAvailable());
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(10)
	public void TestAvailablePositive() {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.available(TEST_SPECIALTY_ID_2, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);

		Specialty specialty = specialtyService.findEntityById(TEST_SPECIALTY_ID_2).get();
		assertEquals(specialty.getAvailable(), true);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(11)
	public void TestAvailableValueNotPresent() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.available(TEST_SPECIALTY_ID_99, model);

		assertEquals(view, VIEW_REDIRECT_OUPS);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(12)
	public void TestNotAvailablePositive() {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.notAvailable(TEST_SPECIALTY_ID_2, model);

		assertEquals(view, VIEW_SPECIALTY_ADMIN_LIST);

		Specialty specialty = specialtyService.findEntityById(TEST_SPECIALTY_ID_2).get();
		assertEquals(specialty.getAvailable(), false);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(13)
	public void TestNotAvailableValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = specialtyAdminController.notAvailable(TEST_SPECIALTY_ID_99, model);

		assertEquals(view, VIEW_REDIRECT_OUPS);
	}

}
