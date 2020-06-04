
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SuggestionAdminControllerE2ETests {

	private static final int	TEST_SUGGESTION_ID_1	= 1;

	private static final int	TEST_SUGGESTION_ID_99	= 99;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(1)
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", false))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(2)
	void testListTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/listTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestions"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", true))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(3)
	void testDetailPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/details/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("suggestion"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("person"))
			.andExpect(MockMvcResultMatchers.model().attribute("isTrash", false))
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/details"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(4)
	void testDetailValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/details/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(5)
	void testReadPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/read/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(6)
	void testReadValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/read/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(7)
	void TestNotReadPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/notRead/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(8)
	void testNotReadValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/notRead/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(9)
	void testMoveTrashPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveTrash/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(10)
	void testMoveTrashValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveTrash/{suggestionId}", TEST_SUGGESTION_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(11)
	void testMoveAllTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/moveAllTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(12)
	void testDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/delete/{suggestionId}", TEST_SUGGESTION_ID_1))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

	@WithMockUser(value = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(13)
	void testDeleteAllTrash() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/suggestion/admin/deleteAllTrash"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("suggestion/admin/list"));
	}

}
