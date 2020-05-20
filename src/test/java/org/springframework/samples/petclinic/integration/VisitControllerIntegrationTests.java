/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class VisitControllerIntegrationTests {

	private int				TEST_PENDING_VISIT_ID	= 4;

	private int				TEST_ACCEPTED_VISIT_ID	= 1;

	private int				TEST_NOT_FOUND_VISIT_ID	= 100;

	private int				TEST_CLINIC_ID			= 1;

	private int				TEST_PET_ID				= 1;

	@Autowired
	private VisitController	visitController;

	@Autowired
	private VisitService	visitService;

	@Autowired
	private ClinicService	clinicService;

	@Autowired
	private PetService		petService;

	@Autowired
	private VetService		vetService;

	@Autowired
	private OwnerService	ownerService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testListAllPending() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPending(modelMap);

		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Visit> list = visitService.findAllPendingByVetId(vet.getId());

		assertNotNull(modelMap.get("visits"));
		assertEquals(((Collection<Visit>) modelMap.get("visits")).size(), list.size());
		((Collection<Visit>) modelMap.get("visits")).forEach(visit -> {
			list.contains(visit);
		});
		assertEquals(false, modelMap.get("accepted"));
		assertNotNull(modelMap.get("message"));

		assertEquals("visits/list", view);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllPendingNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPending(modelMap);

		assertEquals("redirect:/oups", view);
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testListAllAccepted() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllAccepted(modelMap);

		assertEquals(view, "visits/list");
		Vet vet = vetService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Visit> list = visitService.findAllAcceptedByVetId(vet.getId());

		assertNotNull(modelMap.get("visits"));
		assertEquals(((Collection<Visit>) modelMap.get("visits")).size(), list.size());
		((Collection<Visit>) modelMap.get("visits")).forEach(visit -> {
			list.contains(visit);
		});
		assertEquals(true, modelMap.get("accepted"));
		assertNotNull(modelMap.get("message"));

		assertEquals("visits/list", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllAcceptedNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllAccepted(modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	public void testAcceptVisit() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/visits/listAllAccepted", view);

	}

	@WithMockUser(username = "owner3", authorities = {
		"owner"
	})
	@Test
	public void testAcceptVisitAsOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_PENDING_VISIT_ID, modelMap); //owner3 es el dueño del pet al que pertenece visit
		assertEquals("redirect:/oups", view);

	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testAcceptVisitNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/oups", view);

	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	public void testAcceptVisitNotFound() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_NOT_FOUND_VISIT_ID, modelMap);
		assertEquals("redirect:/oups", view);

	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	public void testCancelVisitAsVet() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/visits/listAllAccepted", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testCancelVisitAsVetNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "owner3", authorities = {
		"owner"
	})
	@Test
	public void testCancelVisitAsOwner() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/visits/listByOwner", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testCancelVisitAsOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testInitUpdateVisit() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_ACCEPTED_VISIT_ID, modelMap);

		assertNotNull(modelMap.get("visit"));
		assertEquals(true, modelMap.get("hasClinic"));
		assertEquals("visits/createOrUpdateVisitForm", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testInitUpdateVisitNotFound() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_NOT_FOUND_VISIT_ID, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	public void testInitUpdateVisitAsVetNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_ACCEPTED_VISIT_ID, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testInitUpdateVisitAsOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_ACCEPTED_VISIT_ID, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateVisit() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().plusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);

		assertEquals("Visit succesfully updated", modelMap.getAttribute("messageSuccesful"));
		assertEquals("visits/list", view);

	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateVisitWrongDate() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().minusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);

		//assertEquals("Minimum 2 days after today", result.getFieldError("dateInFuture"));
		assertEquals(entity, modelMap.getAttribute("visit"));
		assertEquals("visits/createOrUpdateVisitForm", view);
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateVisitAsVetNotAuthorized() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().plusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);

		//assertEquals("Minimum 2 days after today", result.getFieldError("dateInFuture"));
		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testUpdateVisitAsOwnerNotAuthorized() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().plusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);

		//assertEquals("Minimum 2 days after today", result.getFieldError("dateInFuture"));
		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateVisitNotFound() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = new Visit();
		String view = visitController.updateVisit(TEST_NOT_FOUND_VISIT_ID, entity, result, modelMap);

		assertEquals("redirect:/oups", view);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testCreateVisit() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Clinic clinic = clinicService.findEntityById(TEST_CLINIC_ID).get();
		Pet pet = petService.findEntityById(TEST_PET_ID).get();
		Visit entity = new Visit();
		entity.setDescription("Description Integration Test");
		entity.setDateTime(LocalDateTime.now().plusMonths(4L));
		entity.setClinic(clinic);
		entity.setPet(pet);
		String view = visitController.createVisit(entity, result, modelMap);

		assertEquals("Visit succesfully created", modelMap.getAttribute("message"));
		assertEquals("redirect:/visits/listByOwner", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testCreateVisitWrongDate() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Clinic clinic = clinicService.findEntityById(TEST_CLINIC_ID).get();
		Pet pet = petService.findEntityById(TEST_PET_ID).get();
		Visit entity = new Visit();
		entity.setDescription("Description Integration Test");
		entity.setDateTime(LocalDateTime.now().minusMonths(4L));
		entity.setClinic(clinic);
		entity.setPet(pet);
		String view = visitController.createVisit(entity, result, modelMap);

		//assertEquals("Minimum 2 days after today", result.getFieldError("dateInFuture"));
		assertEquals(entity, modelMap.getAttribute("visit"));
		assertEquals("visits/createOrUpdateVisitForm", view);

	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testCreateVisitAsVetNotAuthorized() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Clinic clinic = clinicService.findEntityById(TEST_CLINIC_ID).get();
		Pet pet = petService.findEntityById(TEST_PET_ID).get();
		Visit entity = new Visit();
		entity.setDescription("Description Integration Test");
		entity.setDateTime(LocalDateTime.now().minusMonths(4L));
		entity.setClinic(clinic);
		entity.setPet(pet);
		String view = visitController.createVisit(entity, result, modelMap);

		assertEquals("redirect:/oups", view);

	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllPendingAndAcceptedByOwner() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPendingAndAcceptedByOwner(modelMap);
		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());

		Collection<Visit> listPending = visitService.findAllPendingByOwnerId(owner.getId());
		assertNotNull(modelMap.getAttribute("visitsPending"));
		assertEquals(((Collection<Visit>) modelMap.get("visitsPending")).size(), listPending.size());
		((Collection<Visit>) modelMap.get("visitsPending")).forEach(visit -> {
			listPending.contains(visit);
		});

		Collection<Visit> listAccepted = visitService.findAllAcceptedByOwnerId(owner.getId());
		assertNotNull(modelMap.getAttribute("visitsAccepted"));
		assertEquals(((Collection<Visit>) modelMap.get("visitsAccepted")).size(), listAccepted.size());
		((Collection<Visit>) modelMap.get("visitsAccepted")).forEach(visit -> {
			listAccepted.contains(visit);
		});

		assertEquals("visits/listByOwner", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testListAllPendingAndAcceptedByOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPendingAndAcceptedByOwner(modelMap);

		assertEquals("redirect:/oups", view);

	}

}
