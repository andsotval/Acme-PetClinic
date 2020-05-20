
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
public class SpecialtyAdminControllerE2ETests {

	private static final int	TEST_SPECIALTY_ID_1					= 1;

	private static final int	TEST_SPECIALTY_ID_99				= 99;

	private static final String	VIEW_REDIRECT_OUPS					= "redirect:/oups";

	private static final String	VIEW_SPECIALTY_ADMIN_LIST			= "/specialty/admin/list";

	private static final String	VIEW_SPECIALTY_ADMIN_CREATEORUPDATE	= "specialty/admin/createOrUpdateSpecialtyForm";

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", true))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testListNotAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/listNotAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", false))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialty"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessCreationFormNegativeNameIsEmpty() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("specialty"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("specialty", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("specialty", "name"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateFormPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialty"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testInitUpdateFormValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormNegativeNameIsEmpty() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("specialty"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("specialty", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("specialty", "name"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testProcessUpdateFormValueSpecialtyNotPresent() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_99)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/available/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("available"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/available/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testNotAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/notAvailable/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("available"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	void testNotAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/notAvailable/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

}
