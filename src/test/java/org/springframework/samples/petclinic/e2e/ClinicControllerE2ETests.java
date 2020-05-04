
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

	@Autowired
	private MockMvc mockMvc;


	//Show Clinic from a Vet
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}

	//Show Clinic from a Owner
	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testShowClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			//.andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	//Show Clinic from a Owner
	@WithMockUser(value = "owner9", authorities = {
		"owner"
	})
	@Test
	void testShowListClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk())
			//.andExpect(MockMvcResultMatchers.model().attributeExists("clinics"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}

}
