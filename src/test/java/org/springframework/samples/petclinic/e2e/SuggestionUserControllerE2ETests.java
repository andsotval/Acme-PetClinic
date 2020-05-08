
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
public class SuggestionUserControllerE2ETests {

	private static final int	TEST_SUGGESTION_ID_1	= 1;

	private static final int	TEST_SUGGESTION_ID_2	= 2;

	private static final int	TEST_SUGGESTION_ID_3	= 3;

	private static final int	TEST_SUGGESTION_ID_99	= 99;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDetailPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/details/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/details"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDetailValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/details/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testCreate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/new"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/createSuggestionForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testSavePositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/suggestion/user/save")
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "name1")
				.param("description", "description1").param("created", "2020/05/26 12:00:00").param("isRead", "false")
				.param("isTrash", "false").param("isAvailable", "true").param("user", "26"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testSaveNegative() throws Exception {
		mockMvc
			.perform(
				MockMvcRequestBuilders.post("/suggestion/user/save").with(SecurityMockMvcRequestPostProcessors.csrf())
					.param("name", "").param("description", "").param("created", "2020/02/26").param("isRead", "false")
					.param("isTrash", "false").param("isAvailable", "true")/* .param("user", "true") */)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("suggestion"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("suggestion", "name"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("suggestion", "description"))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/createSuggestionForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/delete/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDeleteAll() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/user/deleteAll"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/user/list"));
	}
}
