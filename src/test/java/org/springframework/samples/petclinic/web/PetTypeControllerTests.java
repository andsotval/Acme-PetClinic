
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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = PetTypeController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class PetTypeControllerTests {

	private static final String	VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM	= "/pettype/createOrUpdatePettypeForm";

	private static final String	VIEWS_PETTYPE_LIST					= "/pettype/list";

	private static final int	TEST_PETTYPE_ID_1					= 1;

	private static final int	TEST_PETTYPE_ID_2					= 2;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private PetTypeService		petTypeService;


	@BeforeEach
	void setup() {
		PetType type1 = new PetType();
		type1.setId(TEST_PETTYPE_ID_1);
		type1.setName("type1");
		type1.setAvailable(true);

		PetType type2 = new PetType();
		type2.setId(TEST_PETTYPE_ID_2);
		type2.setName("type2");
		type2.setAvailable(true);

		BDDMockito.given(petTypeService.findAvailable()).willReturn(Lists.newArrayList(type1, type2));

		Optional<PetType> opType = Optional.of(type1);
		BDDMockito.given(petTypeService.findEntityById(TEST_PETTYPE_ID_1)).willReturn(opType);
	}

	@WithMockUser(value = "spring")
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/list")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/new")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettype"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationForm() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormNegative() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettype"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateForm() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_LIST));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormNegative() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", ""))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_CREATE_OR_UPDATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/delete/{pettypeId}", TEST_PETTYPE_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETTYPE_LIST));
	}

}
