
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.ClinicController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClinicControllerIntegrationTests {

	@Autowired
	private ClinicController clinicController;


	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	public void testShowClinicVet() {
		ModelMap model = new ModelMap();
		String view = clinicController.getDetail(model);

		assertEquals(view, "/clinics/clinicDetails");
		//assertNotNull(model.get("clinic"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicOwner() {
		ModelMap model = new ModelMap();
		String view = clinicController.initClinicView(model);

		assertEquals(view, "redirect:/clinics/getDetail");
		//assertNotNull(model.get("clinic"));
	}

	@WithMockUser(username = "owner9", authorities = {
		"owner"
	})
	@Test
	public void testShowListClinicOwner() {
		ModelMap model = new ModelMap();
		String view = clinicController.initClinicView(model);

		assertEquals(view, "/clinics/owner/clinicsList");
		assertNotNull(model.get("clinics"));
	}

}
