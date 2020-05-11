
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetControllerIntegrationTests {

	@Autowired
	private VetController		vetController;

	@Autowired
	private ClinicService		clinicService;

	@Autowired
	private VetService			vetService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	//	@WithMockUser(username = "provider1", authorities = {
	//		"provider"
	//	})
	//	@Test
	//	public void testNegativeShowVetList() {
	//		ModelMap modelMap = new ModelMap();
	//		String view = vetController.showVetList(modelMap);
	//
	//	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testVetsAvailableAndOwnList() {
		ModelMap modelMap = new ModelMap();

		String view = vetController.vetsAvailableAndOwnList(modelMap);

		assertNotNull(modelMap.get("vets2"));
		assertNotNull(modelMap.get("hiredVets"));
		assertEquals(view, "vets/vetsAvailable");
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testVetsAvailableAndOwnListNotAuthorized() {
		ModelMap modelMap = new ModelMap();

		String view = vetController.vetsAvailableAndOwnList(modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testAcceptVetAsManager() {
		ModelMap modelMap = new ModelMap();
		int vetId = 6;

		String view = vetController.acceptVet(vetId, modelMap);

		assertEquals("vets/vetsAvailable", view);
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testAcceptVetAlreadyAcceptedAsManager() {
		ModelMap modelMap = new ModelMap();
		int vetId = 1;

		String view = vetController.acceptVet(vetId, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testAcceptVetNotFoundAsManager() {
		ModelMap modelMap = new ModelMap();
		int vetId = 100;

		String view = vetController.acceptVet(vetId, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testShowVetAsManager() {
		ModelMap modelMap = new ModelMap();
		int vetId = 1;

		String view = vetController.showVet(vetId, modelMap);

		assertNotNull(modelMap.get("vet"));
		assertEquals("vets/vetDetails", view);
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void testShowVetNotFoundAsManager() {
		ModelMap modelMap = new ModelMap();
		int vetId = 100;

		String view = vetController.showVet(vetId, modelMap);

		assertEquals("redirect:/oups", view);
	}

}
