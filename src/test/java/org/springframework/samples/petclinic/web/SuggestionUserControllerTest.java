
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = SuggestionUserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class SuggestionUserControllerTest {

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
		suggestion2.setUser(user);

		Suggestion suggestion3 = new Suggestion();
		suggestion3.setId(TEST_SUGGESTION_ID_3);
		suggestion3.setName("Name 3");
		suggestion3.setDescription("Description 3");
		suggestion3.setCreated(date);
		suggestion3.setIsAvailable(true);
		suggestion3.setIsRead(false);
		suggestion3.setIsTrash(true);

		BDDMockito.given(suggestionService.findAllEntitiesAvailableByUsername("pepito")).willReturn(Lists.newArrayList(suggestion1, suggestion2));

		BDDMockito.given(suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated()).willReturn(Lists.newArrayList(suggestion3));

		Optional<Suggestion> optSuggestion1 = Optional.of(suggestion1);
		BDDMockito.given(suggestionService.findEntityById(TEST_SUGGESTION_ID_1)).willReturn(optSuggestion1);
		BDDMockito.given(suggestionService.saveEntity(suggestion1)).willReturn(suggestion1);

		BDDMockito.given(authoritiesService.findAuthorityByUsername("pepito")).willReturn("owner");
		BDDMockito.given(ownerService.findPersonByUsername("pepito")).willReturn(new Owner());
	}

	@WithMockUser(value = "spring")
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testDetailPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/details/{suggestionId}", TEST_SUGGESTION_ID_1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/details"));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testDetailValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/details/{suggestionId}", TEST_SUGGESTION_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDetailValueNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/details/{suggestionId}", TEST_SUGGESTION_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testCreate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/createSuggestionForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSavePositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/suggestion/user/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1").param("description", "description1").param("created", "2020/05/26 12:00:00").param("isRead", "false")
			.param("isTrash", "false").param("isAvailable", "true")/* .param("user", "26") */).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSaveNegativeNameAndDescriptionNull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/suggestion/user/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "").param("description", "").param("created", "2020/02/26").param("isRead", "false").param("isTrash", "false")
				.param("isAvailable", "true")/* .param("user", "true") */)
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasErrors("suggestion")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("suggestion", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("suggestion", "description")).andExpect(MockMvcResultMatchers.view().name("suggestion/user/createSuggestionForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/delete/{suggestionId}", TEST_SUGGESTION_ID_1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteNegativeNotExistingSuggestion() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/delete/{suggestionId}", TEST_SUGGESTION_ID_99)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteAll() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/deleteAll")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

}
