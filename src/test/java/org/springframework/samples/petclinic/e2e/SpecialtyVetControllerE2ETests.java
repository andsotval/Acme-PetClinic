
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SpecialtyVetControllerE2ETests {

	private static final int	TEST_SPECIALTY_ID_1		= 1;

	private static final int	TEST_SPECIALTY_ID_99	= 99;

	private static final String	VIEW_REDIRECT_OUPS		= "redirect:/oups";

	private static final String	VIEW_SPECIALTY_VET_LIST	= "/specialty/vet/list";

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/add/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "Specialty succesfully added"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAddValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/add/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testRemove() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/remove/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "Specialty succesfully removed"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testRemoveValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/remove/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

}
