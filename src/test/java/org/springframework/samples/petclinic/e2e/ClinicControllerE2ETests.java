
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ClinicControllerE2ETests {
	
	private static final int	TEST_CLINIC_ID				= 1;
	private static final int	TEST_NOT_EXISTING_CLINIC_ID	= 9;

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}
	
	@WithMockUser(value = "vet99", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicVetNotExistingVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testShowClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}
	
	@WithMockUser(value = "owner99",authorities= {"owner"})
	@Test
	void testShowClinicOwnerNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "owner9", authorities = {
		"owner"
	})
	@Test
	void testShowListClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}
	@WithMockUser(value = "owner99",authorities= {"owner"})
	@Test
	void testShowListClinicOwnerNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "owner1", authorities = {
			"owner"
		})
	@Test
	void testUnsubscribeFromClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "owner99",authorities= {"owner"})
	@Test
	void testUnsubscribeFromClinicOwnerNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "owner9", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "owner9", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicOwnerNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
}
