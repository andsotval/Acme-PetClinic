
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
	private static final int	TEST_VET_UNACCEPTED_ID	= 7;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testVetsAvailableAndOwnList() throws Exception {
		mockMvc.perform(get("/vets/vetsAvailable")).andExpect(status().isOk()).andExpect(model().attributeExists("vets2")).andExpect(model().attributeExists("hiredVets")).andExpect(view().name("vets/vetsAvailable"));
	}

	@WithMockUser(value = "manager123", authorities = {
		"manager"
	})
	@Test
	void testVetsAvailableAndOwnListWithManagerNotFound() throws Exception {
		mockMvc.perform(get("/vets/vetsAvailable")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testAcceptVet() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET_UNACCEPTED_ID)).andExpect(status().isOk()).andExpect(view().name("vets/vetsAvailable"));
	}

	//añadir un veterinario con clínica ya asignada
	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testAcceptVetWithAlreadyAssignedClinic() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET2_ID)).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manager1", authorities = {
		"manager"
	})
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET1_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("vet")).andExpect(view().name("vets/vetDetails"));
	}

	//Acceder desde otro tipo usuario
	@WithMockUser(value = "manager123", authorities = {
		"manager"
	})
	@Test
	void testShowVetWithManagerNotPresent() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET1_ID)).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

}
