/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.SuggestionService;
import org.springframework.samples.petclinic.web.SuggestionAdminController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SuggestionAdminControllerIntegrationTests {

	private static final int			TEST_SUGGESTION_ID_1	= 1;

	private static final int			TEST_SUGGESTION_ID_4	= 4;

	@Autowired
	private SuggestionAdminController	suggestionAdminController;

	@Autowired
	private SuggestionService			suggestionService;

	@Autowired
	private AuthoritiesService			authoritiesService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(1)
	public void TestList() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.list(model);

		assertEquals(view, "suggestion/admin/list");

		Collection<Suggestion> list = suggestionService.findAllEntitiesNotTrashOrderByIsReadAndCreated();

		assertNotNull(model.get("suggestions"));

		assertEquals(((Collection<Suggestion>) model.get("suggestions")).size(), list.size());
		((Collection<Suggestion>) model.get("suggestions")).forEach(suggestion -> {
			list.contains(suggestion);
		});

		assertNotNull(model.get("isTrash"));
		assertEquals(model.get("isTrash"), false);
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(2)
	public void TestListTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.listTrash(model);

		assertEquals(view, "suggestion/admin/list");

		Collection<Suggestion> list = suggestionService.findAllEntitiesTrashOrderByIsReadAndCreated();

		assertNotNull(model.get("suggestions"));

		assertEquals(((Collection<Suggestion>) model.get("suggestions")).size(), list.size());
		((Collection<Suggestion>) model.get("suggestions")).forEach(suggestion -> {
			list.contains(suggestion);
		});

		assertNotNull(model.get("isTrash"));
		assertEquals(model.get("isTrash"), true);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(3)
	public void TestDetailPositive() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.detail(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "suggestion/admin/details");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1).get();
		String authority = authoritiesService.findAuthorityByUsername(suggestion.getUser().getUsername());
		assertNotNull(model.get("suggestion"));
		assertEquals(((Suggestion) model.get("suggestion")).getCreated(), suggestion.getCreated());
		assertEquals(((Suggestion) model.get("suggestion")).getDescription(), suggestion.getDescription());
		assertEquals(((Suggestion) model.get("suggestion")).getId(), suggestion.getId());
		assertEquals(((Suggestion) model.get("suggestion")).getIsAvailable(), suggestion.getIsAvailable());
		assertEquals(((Suggestion) model.get("suggestion")).getIsRead(), suggestion.getIsRead());
		assertEquals(((Suggestion) model.get("suggestion")).getIsTrash(), suggestion.getIsTrash());
		assertEquals(((Suggestion) model.get("suggestion")).getName(), suggestion.getName());
		assertEquals(((Suggestion) model.get("suggestion")).getUser().getId(), suggestion.getUser().getId());

		assertNotNull(model.get("authority"));
		assertEquals(model.get("authority"), authority.substring(0, 1).toUpperCase() + authority.substring(1));
		assertNotNull(model.get("person"));

		assertNotNull(model.get("isTrash"));
		assertEquals(model.get("isTrash"), false);

		assertEquals(suggestion.getIsRead(), true);

	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(4)
	public void TestDetailValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.detail(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(5)
	public void TestReadPositive() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.read(TEST_SUGGESTION_ID_4, model);

		assertEquals(view, "suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_4).get();
		assertEquals(suggestion.getIsRead(), true);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(6)
	public void TestReadValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.read(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(7)
	public void TestNotReadPositive() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.notRead(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1).get();
		assertEquals(suggestion.getIsRead(), false);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(8)
	public void TestNotReadValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.notRead(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(9)
	public void TestMoveTrashPositive() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.moveTrash(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1).get();
		assertEquals(suggestion.getIsTrash(), true);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(10)
	public void TestMoveTrashValueNotPresent() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.moveTrash(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(11)
	public void TestMoveAllTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.moveAllTrash(model);

		assertEquals(view, "suggestion/admin/list");

		Collection<Suggestion> suggestions = (Collection<Suggestion>) suggestionService.findAllEntities();
		suggestions.forEach(suggestion -> {
			assertEquals(suggestion.getIsTrash(), true);
		});

	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(12)
	public void TestDelete() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.delete(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "suggestion/admin/list");

		Optional<Suggestion> suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1);
		assertEquals(suggestion.isPresent(), false);
	}

	@WithMockUser(username = "admin", authorities = {
		"admin"
	})
	@Test
	@Order(13)
	public void TestDeleteAllTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.deleteAllTrash(model);

		assertEquals(view, "suggestion/admin/list");

		Collection<Suggestion> suggestions = (Collection<Suggestion>) suggestionService.findAllEntities();
		assertEquals(suggestions.isEmpty(), true);

	}

}
