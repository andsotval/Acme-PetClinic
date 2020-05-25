
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.samples.petclinic.web.SpecialtyVetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SpecialtyVetControllerIntegrationTests {

	private static final int		TEST_SPECIALTY_ID_1		= 1;

	private static final int		TEST_SPECIALTY_ID_99	= 99;

	private static final String		VIEW_REDIRECT_OUPS		= "redirect:/oups";

	private static final String		VIEW_SPECIALTY_VET_LIST	= "/specialty/vet/list";

	@Autowired
	private SpecialtyVetController	specialtyVetController;

	@Autowired
	private SpecialtyService		specialtyService;

	@Autowired
	private VetService				vetService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	@Order(1)
	public void TestList() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyVetController.list(model);

		assertEquals(view, VIEW_SPECIALTY_VET_LIST);

		Collection<Specialty> list = specialtyService.findAvailable();
		assertNotNull(model.get("specialties"));
		assertEquals(((Collection<Specialty>) model.get("specialties")).size(), list.size());
		((Collection<Specialty>) model.get("specialties")).forEach(specialty -> {
			list.contains(specialty);
		});

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Specialty> mySpecialties = vet.getSpecialties();
		assertNotNull(model.get("mySpecialties"));
		assertEquals(((Collection<Specialty>) model.get("mySpecialties")).size(), mySpecialties.size());
		((Collection<Specialty>) model.get("mySpecialties")).forEach(specialty -> {
			mySpecialties.contains(specialty);
		});
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	@Order(2)
	public void TestAdd() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyVetController.add(TEST_SPECIALTY_ID_1, model);

		assertEquals(view, VIEW_SPECIALTY_VET_LIST);

		boolean add = false;

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Specialty> mySpecialties = vet.getSpecialties();
		assertNotNull(model.get("mySpecialties"));
		assertEquals(((Collection<Specialty>) model.get("mySpecialties")).size(), mySpecialties.size());
		((Collection<Specialty>) model.get("mySpecialties")).forEach(specialty -> {
			mySpecialties.contains(specialty);
		});

		for (Specialty s : mySpecialties)
			if (s.getId().equals(TEST_SPECIALTY_ID_1))
				add = true;

		assertEquals(add, true);
	}

	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	@Order(3)
	public void TestAddValueNotPresent() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyVetController.add(TEST_SPECIALTY_ID_99, model);

		assertEquals(view, VIEW_REDIRECT_OUPS);
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	@Order(4)
	@Transactional
	public void TestRemove() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyVetController.remove(TEST_SPECIALTY_ID_1, model);

		assertEquals(view, VIEW_SPECIALTY_VET_LIST);

		boolean remove = true;

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Specialty> mySpecialties = vet.getSpecialties();
		assertNotNull(model.get("mySpecialties"));
		assertEquals(((Collection<Specialty>) model.get("mySpecialties")).size(), mySpecialties.size());
		((Collection<Specialty>) model.get("mySpecialties")).forEach(specialty -> {
			mySpecialties.contains(specialty);
		});

		for (Specialty s : mySpecialties)
			if (s.getId().equals(TEST_SPECIALTY_ID_1))
				remove = false;

		assertEquals(remove, true);
	}

	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	@Order(5)
	public void TestRemoveValueNotPresent() throws Exception {
		ModelMap model = new ModelMap();
		String view = specialtyVetController.remove(TEST_SPECIALTY_ID_99, model);

		assertEquals(view, VIEW_REDIRECT_OUPS);
	}

}
