/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Suggestion;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.SuggestionService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.samples.petclinic.web.SuggestionUserController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class SuggestionUserControllerIntegrationTests {

	private static final int			TEST_SUGGESTION_ID_1	= 1;

	private static Suggestion			suggestion;

	@Autowired
	private SuggestionUserController	suggestionUserController;

	@Autowired
	private SuggestionService			suggestionService;

	@Autowired
	private OwnerService				ownerService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(4)
	public void TestList() {
		ModelMap model = new ModelMap();
		String view = suggestionUserController.list(model);

		assertEquals(view, "suggestion/user/list");

		Collection<Suggestion> list = suggestionService
			.findAllEntitiesAvailableByUsername(SessionUtils.obtainUserInSession().getUsername());

		assertNotNull(model.get("suggestions"));

		assertEquals(((Collection<Suggestion>) model.get("suggestions")).size(), list.size());
		((Collection<Suggestion>) model.get("suggestions")).forEach(suggestion -> {
			list.contains(suggestion);
		});
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(5)
	public void TestDetailPositive() {
		ModelMap model = new ModelMap();
		String view = suggestionUserController.detail(suggestion.getId(), model);

		assertEquals(view, "suggestion/user/details");

		Suggestion suggestion = suggestionService
			.findEntityById(SuggestionUserControllerIntegrationTests.suggestion.getId()).get();
		assertNotNull(model.get("suggestion"));
		assertEquals(((Suggestion) model.get("suggestion")).getCreated(), suggestion.getCreated());
		assertEquals(((Suggestion) model.get("suggestion")).getDescription(), suggestion.getDescription());
		assertEquals(((Suggestion) model.get("suggestion")).getId(), suggestion.getId());
		assertEquals(((Suggestion) model.get("suggestion")).getIsAvailable(), suggestion.getIsAvailable());
		assertEquals(((Suggestion) model.get("suggestion")).getIsRead(), suggestion.getIsRead());
		assertEquals(((Suggestion) model.get("suggestion")).getIsTrash(), suggestion.getIsTrash());
		assertEquals(((Suggestion) model.get("suggestion")).getName(), suggestion.getName());
		assertEquals(((Suggestion) model.get("suggestion")).getUser().getId(), suggestion.getUser().getId());
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(6)
	public void TestDetailNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = suggestionUserController.detail(2, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(1)
	public void TestCreate() {
		ModelMap model = new ModelMap();
		String view = suggestionUserController.create(model);

		assertEquals(view, "suggestion/user/createSuggestionForm");

		assertNotNull(model.get("suggestion"));
		assertEquals(((Suggestion) model.get("suggestion")).getIsAvailable(), true);
		assertEquals(((Suggestion) model.get("suggestion")).getIsRead(), false);
		assertEquals(((Suggestion) model.get("suggestion")).getIsTrash(), false);
		assertEquals(((Suggestion) model.get("suggestion")).getUser().getUsername(), "owner1");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(2)
	public void TestSavePositive() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());

		Suggestion newSuggestion = new Suggestion();
		newSuggestion.setCreated(LocalDateTime.now());
		newSuggestion.setUser(owner.getUser());
		newSuggestion.setIsAvailable(true);
		newSuggestion.setIsRead(false);
		newSuggestion.setIsTrash(false);
		newSuggestion.setName("Title Suggestion Integration Test 1");
		newSuggestion.setDescription("Description 1");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = suggestionUserController.save(newSuggestion, bindingResult, model);

		assertEquals(view, "suggestion/user/list");

		List<Suggestion> suggestions = suggestionService.findAllEntitiesAvailableByUsername("owner1").stream()
			.filter(s -> s.getName().equals("Title Suggestion Integration Test 1")).collect(Collectors.toList());

		assertNotNull(suggestions.get(0));
		suggestion = suggestions.get(0);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(3)
	public void TestSaveNegativeNameAndDescriptionNull() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());

		Suggestion suggestion = new Suggestion();
		suggestion.setCreated(LocalDateTime.now());
		suggestion.setUser(owner.getUser());
		suggestion.setIsAvailable(true);
		suggestion.setIsRead(false);
		suggestion.setIsTrash(false);
		suggestion.setName("");
		suggestion.setDescription("");

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "Requied!");
		bindingResult.reject("description", "Requied!");
		String view = suggestionUserController.save(suggestion, bindingResult, model);

		assertEquals(view, "suggestion/user/createSuggestionForm");

		assertNotNull(model.get("suggestion"));
		assertEquals(((Suggestion) model.get("suggestion")).getIsAvailable(), true);
		assertEquals(((Suggestion) model.get("suggestion")).getIsRead(), false);
		assertEquals(((Suggestion) model.get("suggestion")).getIsTrash(), false);
		assertEquals(((Suggestion) model.get("suggestion")).getUser().getUsername(), "owner1");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(7)
	public void TestDeletePositive() {
		ModelMap model = new ModelMap();

		String view = suggestionUserController.delete(suggestion.getId(), model);

		assertEquals(view, "suggestion/user/list");

		Suggestion suggestion = suggestionService
			.findEntityById(SuggestionUserControllerIntegrationTests.suggestion.getId()).get();
		assertEquals(suggestion.getIsAvailable(), false);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(8)
	public void TestDeleteNotAuthorizated() {
		ModelMap model = new ModelMap();

		String view = suggestionUserController.delete(2, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(9)
	public void TestDeleteAll() {
		ModelMap model = new ModelMap();

		String view = suggestionUserController.deleteAll(model);

		assertEquals(view, "suggestion/user/list");

		Collection<Suggestion> suggestions = suggestionService.findAllEntitiesAvailableByUsername("owner1");
		suggestions.forEach(suggestion -> {
			assertEquals(suggestion.getIsAvailable(), false);
		});

	}
}
