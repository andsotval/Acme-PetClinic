
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

@WebMvcTest(value = PetTypeController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PetTypeControllerTests {

	private static final int	TEST_PETTYPE_ID_1	= 1;

	private static final int	TEST_PETTYPE_ID_2	= 2;

	private static final int	TEST_PETTYPE_ID_99	= 99;

	private String				VIEW_REDIRECT_OUPS	= "redirect:/oups";

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
	void testListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/listAvailable")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", true)).andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotListAvailable() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/listNotAvailable")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pettypes"))
			.andExpect(MockMvcResultMatchers.model().attribute("available", false)).andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("petType"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/pettype/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("petType"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("petType")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "available")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("petType", "name"))
			.andExpect(MockMvcResultMatchers.view().name("pettype/createOrUpdatePettypeForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/pettype/edit/{pettypeId}", TEST_PETTYPE_ID_99).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("available", "true"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/available/{pettypeId}", TEST_PETTYPE_ID_1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/available/{pettypeId}", TEST_PETTYPE_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotAvailablePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/notAvailable/{pettypeId}", TEST_PETTYPE_ID_1)).andExpect(MockMvcResultMatchers.model().attributeExists("pettypes")).andExpect(MockMvcResultMatchers.model().attributeExists("available"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/pettype/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotAvailableValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/pettype/notAvailable/{pettypeId}", TEST_PETTYPE_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

}
