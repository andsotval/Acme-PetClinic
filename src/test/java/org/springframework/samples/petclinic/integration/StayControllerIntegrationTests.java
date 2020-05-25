/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.StayController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class StayControllerIntegrationTests {

	private int					TEST_PENDING_VISIT_ID	= 4;

	private int					TEST_ACCEPTED_VISIT_ID	= 1;

	private int					TEST_NOTFOUND_VISIT_ID	= 100;

	private int					TEST_CLINIC_ID			= 1;

	private int					TEST_PET_ID				= 1;

	@Autowired
	private StayController		stayController;

	@Autowired
	private StayService			stayService;

	@Autowired
	private VetService			vetService;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet6", authorities = {
		"veterinarian"
	})
	@Test
	public void testListAllPending() {
		ModelMap modelMap = new ModelMap();
		String view = stayController.listAllPending(modelMap);

		assertEquals(view, "stays/list");
		assertEquals(false, modelMap.get("accepted"));
		assertNotNull(modelMap.get("stays"));
		((Collection<Stay>) modelMap.get("stays")).forEach((stay) -> assertTrue(!stay.getIsAccepted()));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllPendingAsOwnerNegativeNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		String view = stayController.listAllPending(modelMap);

		assertEquals(view, "redirect:/oups");

	}

	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testListAllAccepted() {
		ModelMap modelMap = new ModelMap();
		String view = stayController.listAllAccepted(modelMap);

		assertEquals(view, "stays/list");
		assertEquals(true, modelMap.get("accepted"));
		assertNotNull(modelMap.get("stays"));
		((Collection<Stay>) modelMap.get("stays")).forEach((stay) -> assertTrue(stay.getIsAccepted()));

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllAcceptedAsOwnerNegative() {
		ModelMap modelMap = new ModelMap();
		String view = stayController.listAllAccepted(modelMap);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testAcceptStay() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.acceptStay(stayId, modelMap);

		Optional<Stay> stay = stayService.findEntityById(stayId);

		assertTrue(stay.get().getIsAccepted());
		assertEquals(view, "redirect:/stays/listAllAccepted");
	}

	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	public void testAcceptStayNotAuthorizedNegative() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.acceptStay(stayId, modelMap);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testCancelStayAsVet() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.cancelStay(stayId, modelMap);

		Optional<Stay> stay = stayService.findEntityById(stayId);

		assertEquals(false, stay.get().getIsAccepted());
		assertEquals(view, "redirect:/stays/listAllAccepted");
	}

	@WithMockUser(username = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	public void testCancelStayAsVetNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.cancelStay(stayId, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testCancelStayAsOwner() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.cancelStay(stayId, modelMap);

		Optional<Stay> stay = stayService.findEntityById(stayId);

		assertEquals(false, stay.get().getIsAccepted());
		assertEquals("redirect:/stays/listByOwner", view);
	}

	@WithMockUser(username = "owner3", authorities = {
		"owner"
	})
	@Test
	public void testCancelStayAsOwnerNotAuthorized() {
		ModelMap modelMap = new ModelMap();
		int stayId = 1;
		String view = stayController.cancelStay(stayId, modelMap);

		assertEquals("redirect:/oups", view);
	}

	@WithMockUser(username = "vet3", authorities = {
		"veterinarian"
	})
	@Test
	public void testShowChangeDateStay() {
		ModelMap modelMap = new ModelMap();
		int stayId = 3;
		String view = stayController.changeDateStay(stayId, modelMap);
		assertNotNull(modelMap.get("stay"));
		assertEquals(true, modelMap.get("hasClinic"));
		assertEquals("stays/createOrUpdateStayForm", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateStaySuccesful() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L));
		stay.setFinishDate(LocalDate.now().plusMonths(5L).plusDays(5L));

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertEquals("Stay succesfully updated", modelMap.get("message"));
		assertEquals("stays/list", view);
	}
	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testUpdateStayNegativeNullDate() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(null);
		stay.setFinishDate(null);

		result.reject("startDate", "NotNull");
		result.reject("finishDate", "NotNull");

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertNotNull(modelMap.get("hasClinic"));
		assertEquals("stays/createOrUpdateStayForm", view);
	}

	@WithMockUser(username = "vet5", authorities = {
		"veterinarian"
	})
	@Test
	public void testNegativeUpdateStayUnauthorized() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L));
		stay.setFinishDate(null);

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testNegativeUpdateStay2DaysFromToday() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now());
		stay.setFinishDate(LocalDate.now().plusDays(1L));

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertEquals(modelMap.get("hasClinic"), true);
		assertEquals("stays/createOrUpdateStayForm", view);
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testNegativeUpdateCheckMoreThanOneWeek() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L));
		stay.setFinishDate(LocalDate.now().plusMonths(5L).plusDays(9L));

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertEquals("stays/createOrUpdateStayForm", view);
		assertEquals(true, modelMap.get("hasClinic"));
		assertNotNull(modelMap.get("stay"));

	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	public void testNegativeUpdateCheckFinishBeforeStart() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(30);

		Pet pet = new Pet();
		pet.setId(30);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L).plusDays(9L));
		stay.setFinishDate(LocalDate.now().plusMonths(5L));

		String view = stayController.updateStay(stayId, stay, result, modelMap);

		assertEquals("stays/createOrUpdateStayForm", view);
		assertEquals(true, modelMap.get("hasClinic"));
		assertNotNull(modelMap.get("stay"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testNewStayPositive() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(2);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(1L));
		stay.setFinishDate(LocalDate.now().plusMonths(1L).plusDays(3L));

		String view = stayController.newStay(stay, result, modelMap);

		assertEquals(view, "redirect:/stays/listByOwner");
		assertEquals(modelMap.get("message"), "Stay succesfully updated");

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testNewStayNegativeStay2DaysFromToday() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(2);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now());
		stay.setFinishDate(LocalDate.now().plusDays(1L));

		String view = stayController.newStay(stay, result, modelMap);

		assertEquals(view, "stays/createOrUpdateStayForm");
		assertNotNull(modelMap.get("stays"));
		assertEquals(modelMap.get("hasClinic"), true);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testNewStayNegativeCheckMoreThanOneWeek() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(2);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L));
		stay.setFinishDate(LocalDate.now().plusMonths(5L).plusDays(9L));

		String view = stayController.newStay(stay, result, modelMap);

		assertEquals(view, "stays/createOrUpdateStayForm");
		assertNotNull(modelMap.get("stays"));
		assertEquals(modelMap.get("hasClinic"), true);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testNewStayNegativeCheckFinishBeforeStart() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(2);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(LocalDate.now().plusMonths(5L).plusDays(9L));
		stay.setFinishDate(LocalDate.now().plusMonths(5L));

		String view = stayController.newStay(stay, result, modelMap);

		assertEquals(view, "stays/createOrUpdateStayForm");
		assertNotNull(modelMap.get("stays"));
		assertEquals(modelMap.get("hasClinic"), true);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testNewStayNegativeNullDate() {
		ModelMap modelMap = new ModelMap();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.reject("finishDate", "Not Null");
		result.reject("startDate", "Not Null");

		int stayId = 1;

		Clinic clinic = new Clinic();
		clinic.setId(2);

		Pet pet = new Pet();
		pet.setId(2);
		pet.setName("PetName");

		Stay stay = new Stay();
		stay.setId(stayId);
		stay.setDescription("Description of the stay");
		stay.setClinic(clinic);
		stay.setIsAccepted(true);
		stay.setPet(pet);
		stay.setStartDate(null);
		stay.setFinishDate(null);

		String view = stayController.newStay(stay, result, modelMap);

		assertEquals(view, "stays/createOrUpdateStayForm");
		assertNotNull(modelMap.get("stays"));
		assertEquals(modelMap.get("hasClinic"), true);
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testListAllPendingByOwner() {
		ModelMap modelMap = new ModelMap();

		String view = stayController.listAllPendingByOwner(modelMap);

		assertNotNull("staysPending");
		assertNotNull("staysAccepted");
		assertEquals("stays/listByOwner", view);
	}

	@WithMockUser(username = "provider1", authorities = {
		"provider"
	})
	@Test
	public void testNegativeListAllPendingByOtherUser() {
		ModelMap modelMap = new ModelMap();

		String view = stayController.listAllPendingByOwner(modelMap);

		assertEquals("redirect:/oups", view);
	}

}
