/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.service.SuggestionService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(value = SuggestionAdminController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class SuggestionAdminControllerTests {

	private static final int	TEST_SUGGESTION_ID_1	= 1;

	private static final int	TEST_SUGGESTION_ID_2	= 2;

	private static final int	TEST_SUGGESTION_ID_3	= 3;

	private static final int	TEST_SUGGESTION_ID_99	= 99;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private SuggestionService	suggestionService;
	@MockBean
	private AuthoritiesService	authoritiesService;
	@MockBean
	private ManagerService		managerService;
	@MockBean
	private OwnerService		ownerService;
	@MockBean
	private ProviderService		providerService;
	@MockBean
	private VetService			vetService;


	@BeforeEach
	void setup() {

		LocalDateTime date = LocalDateTime.now();

		Suggestion suggestion1 = new Suggestion();
		suggestion1.setId(TEST_SUGGESTION_ID_1);
		suggestion1.setName("Name 1");
		suggestion1.setDescription("Description 1");
		suggestion1.setCreated(date);
		suggestion1.setIsAvailable(true);
		suggestion1.setIsRead(false);
		suggestion1.setIsTrash(false);
		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");
		suggestion1.setUser(user);

		Suggestion suggestion2 = new Suggestion();
		suggestion2.setId(TEST_SUGGESTION_ID_2);
		suggestion2.setName("Name 2");
		suggestion2.setDescription("Description 2");
		suggestion2.setCreated(date);
		suggestion2.setIsAvailable(true);
		suggestion2.setIsRead(false);
		suggestion2.setIsTrash(false);

		Suggestion suggestion3 = new Suggestion();
		suggestion3.setId(TEST_SUGGESTION_ID_3);
		suggestion3.setName("Name 3");
		suggestion3.setDescription("Description 3");
		suggestion3.setCreated(date);
		suggestion3.setIsAvailable(true);
		suggestion3.setIsRead(false);
		suggestion3.setIsTrash(true);

		BDDMockito.given(suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated())
			.willReturn(Lists.newArrayList(suggestion1, suggestion2));

		BDDMockito.given(suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated())
			.willReturn(Lists.newArrayList(suggestion3));

		Optional<Suggestion> optSuggestion1 = Optional.of(suggestion1);
		BDDMockito.given(suggestionService.findEntityById(TEST_SUGGESTION_ID_1)).willReturn(optSuggestion1);
		BDDMockito.given(suggestionService.saveEntity(suggestion1)).willReturn(suggestion1);

		BDDMockito.given(authoritiesService.findAuthorityByUsername("pepito")).willReturn("owner");
		BDDMockito.given(ownerService.findPersonByUsername("pepito")).willReturn(new Owner());
	}

	@WithMockUser(value = "spring")
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", false))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/listTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", true))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDetailPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/details/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("person"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", false))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/details"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDetailValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/details/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testReadPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/read/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testReadValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/read/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotReadPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/notRead/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testNotReadValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/notRead/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testMoveTrashPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveTrash/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testMoveTrashValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveTrash/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testMoveAllTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveAllTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/delete/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteAllTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/deleteAllTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

}
