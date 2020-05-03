package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerIntegrationTests {
	
	private int TEST_PENDING_VISIT_ID = 4;
	
	private int TEST_ACCEPTED_VISIT_ID = 1;
	
	private int TEST_NOTFOUND_VISIT_ID = 100;

	@Autowired
	private VisitController		visitController;
	
	@Autowired
	private VisitService		visitService;
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testListAllPending() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPending(modelMap);

		assertEquals(view, "visits/list");
		assertEquals(false, modelMap.get("accepted"));
		assertNotNull(modelMap.get("visits"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testListAllPendingNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllPending(modelMap);

		assertEquals(view, "redirect:/oups");
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testListAllAccepted() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllAccepted(modelMap);

		assertEquals(view, "visits/list");
		assertEquals(true, modelMap.get("accepted"));
		assertNotNull(modelMap.get("visits"));
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testListAllAcceptedNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.listAllAccepted(modelMap);

		assertEquals(view, "redirect:/oups");
	}
	
	@WithMockUser(username="vet4",authorities= {"veterinarian"})
	@Test
	public void testAcceptVisit() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(view, "redirect:/visits/listAllAccepted");

	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testAcceptVisitNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.acceptVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(true, modelMap.get("nonAuthorized"));
		assertEquals(view, "redirect:/visits/listAllAccepted");

	}
	
	@WithMockUser(username="vet4",authorities= {"veterinarian"})
	@Test
	public void testCancelVisitAsVet() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(view, "redirect:/visits/listAllAccepted");
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testCancelVisitAsVetNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(true, modelMap.get("notAuthorizedVet"));
		assertEquals(view, "redirect:/visits/listAllAccepted");
	}
	
	@WithMockUser(username="owner3",authorities= {"owner"})
	@Test
	public void testCancelVisitAsOwner() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(view, "redirect:/visits/listByOwner");
	}
	
	@WithMockUser(username="owner4",authorities= {"owner"})
	@Test
	public void testCancelVisitAsOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.cancelVisit(TEST_PENDING_VISIT_ID, modelMap);
		assertEquals(true, modelMap.get("notAuthorizedOwner"));
		assertEquals(view, "redirect:/visits/listByOwner");
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testInitUpdateVisit() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_ACCEPTED_VISIT_ID, modelMap);
		
		assertNotNull(modelMap.get("visit"));
		assertEquals(view, "/visits/createOrUpdateVisitForm");
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testInitUpdateVisitNotFound() {
		ModelMap modelMap = new ModelMap();
		String view = visitController.initUpdateVisit(TEST_NOTFOUND_VISIT_ID, modelMap);
		
		assertEquals(view, "redirect:/oups");
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testUpdateVisit() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = this.visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().plusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);
		
		assertEquals("Visit succesfully updated", modelMap.getAttribute("messageSuccesful"));
		assertEquals(view, "visits/list");
		
	}
	
	@WithMockUser(username="vet1",authorities= {"veterinarian"})
	@Test
	public void testUpdateVisitWrongDate() {
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		ModelMap modelMap = new ModelMap();
		Visit entity = this.visitService.findEntityById(TEST_ACCEPTED_VISIT_ID).get();
		entity.setDescription("CHANGING DESCRIPTION");
		entity.setDateTime(LocalDateTime.now().minusMonths(1L));
		String view = visitController.updateVisit(TEST_ACCEPTED_VISIT_ID, entity, result, modelMap);
		
		//assertEquals("Minimum 2 days after today", result.getFieldError("dateInFuture"));
		assertEquals(entity, modelMap.getAttribute("visit"));
		assertEquals(view, "/visits/createOrUpdateVisitForm");
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testCreateVisit() {
		
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testCreateVisitWrongDate() {
		
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testListAllPendingAndAcceptedByOwner() {
		
	}
	
	@WithMockUser(username="owner1",authorities= {"owner"})
	@Test
	public void testListAllPendingAndAcceptedByOwnerNotAuthorized() {
		
	}
	
	
	
	
	
	
}
