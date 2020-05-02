package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.VisitController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerIntegrationTests {

	@Autowired
	private VisitController		visitController;
	
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
	
}
