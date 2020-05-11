
package org.springframework.samples.petclinic.e2e;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class VetControllerE2ETests {

	private static final int	TEST_VET1_ID			= 1;
	private static final int	TEST_VET2_ID			= 2;
	private static final int	TEST_VET_NOT_FOUND_ID	= 99;
	private static final int	TEST_VET_UNACCEPTED_ID	= 7;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testVetsAvailableAndOwnList() throws Exception {
		mockMvc.perform(get("/vets/vetsAvailable")).andExpect(status().isOk())
			.andExpect(model().attributeExists("vets2")).andExpect(model().attributeExists("hiredVets"))
			.andExpect(view().name("vets/vetsAvailable"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testVetsAvailableAndOwnListNegativeNotAuthorized() throws Exception {
		mockMvc.perform(get("/vets/vetsAvailable")).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testAcceptVetAsManager() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET_UNACCEPTED_ID)).andExpect(status().isOk())
			.andExpect(view().name("vets/vetsAvailable"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testAcceptVetAlreadyAcceptedAsManager() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET2_ID)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testAcceptVetNotFoundAsManager() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET_NOT_FOUND_ID)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowVetAsManager() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET1_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("vet")).andExpect(view().name("vets/vetDetails"));
	}

	@WithMockUser(value = "manager123", authorities = {
		"manager"
	})
	@Test
	void testShowVetNotFoundAsManager() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET1_ID)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

}
