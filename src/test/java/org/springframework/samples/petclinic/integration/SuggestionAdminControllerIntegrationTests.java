
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
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

	@Test
	@Order(3)
	public void TestDetail() {
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

	@Test
	@Order(4)
	public void TestRead() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.read(TEST_SUGGESTION_ID_4, model);

		assertEquals(view, "redirect:/suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_4).get();
		assertEquals(suggestion.getIsRead(), true);
	}

	@Test
	@Order(5)
	public void TestNotRead() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.notRead(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "redirect:/suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1).get();
		assertEquals(suggestion.getIsRead(), false);
	}

	@Test
	@Order(6)
	public void TestMoveTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.moveTrash(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "redirect:/suggestion/admin/list");

		Suggestion suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1).get();
		assertEquals(suggestion.getIsTrash(), true);
	}

	@Test
	@Order(7)
	public void TestMoveAllTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.moveAllTrash(model);

		assertEquals(view, "redirect:/suggestion/admin/list");

		Collection<Suggestion> suggestions = (Collection<Suggestion>) suggestionService.findAllEntities();
		suggestions.forEach(suggestion -> {
			assertEquals(suggestion.getIsTrash(), true);
		});

	}

	@Test
	@Order(8)
	public void TestDelete() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.delete(TEST_SUGGESTION_ID_1, model);

		assertEquals(view, "redirect:/suggestion/admin/listTrash");

		Optional<Suggestion> suggestion = suggestionService.findEntityById(TEST_SUGGESTION_ID_1);
		assertEquals(suggestion.isPresent(), false);
	}

	@Test
	@Order(9)
	public void TestDeleteAllTrash() {
		ModelMap model = new ModelMap();
		String view = suggestionAdminController.deleteAllTrash(model);

		assertEquals(view, "redirect:/suggestion/admin/listTrash");

		Collection<Suggestion> suggestions = (Collection<Suggestion>) suggestionService.findAllEntities();
		assertEquals(suggestions.isEmpty(), true);

	}
}
