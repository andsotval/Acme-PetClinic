
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class PetTypeControllerE2ETests {

	private static final int	TEST_PETTYPE_ID_1	= 1;

	private static final int	TEST_PETTYPE_ID_99	= 99;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", true))
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testNotListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/listNotAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", false))
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("petType"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationForm() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormNotName() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("petType"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateFormValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateNotName() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormValueNotPresent() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_99)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/available/{pettypeId}", TEST_PETTYPE_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/available/{pettypeId}", TEST_PETTYPE_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testNotAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/notAvailable/{pettypeId}", TEST_PETTYPE_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testNotAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/notAvailable/{pettypeId}", TEST_PETTYPE_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}
