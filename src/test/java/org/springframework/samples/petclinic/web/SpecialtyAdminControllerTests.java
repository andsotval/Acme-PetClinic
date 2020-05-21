/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(value = SpecialtyAdminController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class SpecialtyAdminControllerTests {

	private static final int	TEST_SPECIALTY_ID_1					= 1;

	private static final int	TEST_SPECIALTY_ID_2					= 2;

	private static final int	TEST_SPECIALTY_ID_99				= 99;

	private static final String	VIEW_REDIRECT_OUPS					= "redirect:/oups";

	private static final String	VIEW_SPECIALTY_ADMIN_LIST			= "/specialty/admin/list";

	private static final String	VIEW_SPECIALTY_ADMIN_CREATEORUPDATE	= "specialty/admin/createOrUpdateSpecialtyForm";

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private SpecialtyService	specialtyService;


	@BeforeEach
	void setup() {
		Specialty specialty1 = new Specialty();
		specialty1.setId(TEST_SPECIALTY_ID_1);
		specialty1.setName("type1");
		specialty1.setAvailable(true);

		Specialty specialty2 = new Specialty();
		specialty2.setId(TEST_SPECIALTY_ID_2);
		specialty2.setName("type2");
		specialty2.setAvailable(true);

		BDDMockito.given(specialtyService.findAvailable()).willReturn(Lists.newArrayList(specialty1, specialty2));

		Optional<Specialty> optSpecialty = Optional.of(specialty1);
		BDDMockito.given(specialtyService.findEntityById(TEST_SPECIALTY_ID_1)).willReturn(optSpecialty);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/listAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", true))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListNotAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/listNotAvailable"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", false))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialty"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
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

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialty"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_CREATEORUPDATE));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
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

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormValueSpecialtyNotPresent() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/specialty/admin/edit/{specialtyId}", TEST_SPECIALTY_ID_99)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/available/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("available"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/available/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/notAvailable/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("available"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_ADMIN_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/admin/notAvailable/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}
}
