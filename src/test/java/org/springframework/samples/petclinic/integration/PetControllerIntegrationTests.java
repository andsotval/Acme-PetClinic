
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PetControllerIntegrationTests {

	private static final int	TEST_OWNER_ID		= 1;
	private static final int	TEST_OWNER_ID_2		= 2;
	private static final int	TEST_PET_ID			= 1;
	private static final int	TEST_PET_ID_2		= 10;
	private static final int	TEST_PET_ID_WRONG	= 999;

	@Autowired
	private PetController		petController;

	@Autowired
	private PetService			petService;

	@Autowired
	private PetTypeService		petTypeService;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private VisitService		visitService;

	@Autowired
	private StayService			stayService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(1)
	public void TestListMyPetsPositive() {
		ModelMap model = new ModelMap();
		String view = petController.listMyPets(model);

		assertEquals("pets/list", view);

		Owner owner = ownerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Collection<Pet> list = (Collection<Pet>) petService.findPetsByOwnerId(owner.getId());
		assertNotNull(model.get("pets"));
		assertNotNull(model.get("ownerId"));
		assertEquals(((Collection<Pet>) model.get("pets")).size(), list.size());
		((Collection<Pet>) model.get("pets")).forEach(pet -> {
			list.contains(pet);
		});
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(5)
	public void TestNewPetPositive() {
		ModelMap model = new ModelMap();
		String view = petController.newPet(TEST_OWNER_ID, model);

		assertEquals("pets/createOrUpdatePetForm", view);
		assertNotNull(model.get("pet"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(6)
	public void TestSavePetPositive() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());
		PetType type = petTypeService.findAvailable().iterator().next();

		Pet newPet = new Pet();
		LocalDate date = LocalDate.of(2019, 8, 11);
		newPet.setBirthDate(date);
		newPet.setName("newPet");
		newPet.setOwner(owner);
		newPet.setType(type);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.savePet(newPet, bindingResult, model);

		assertEquals(view, "redirect:/pets/listMyPets");

		List<Pet> pets = ((Collection<Pet>) petService.findPetsByOwnerId(TEST_OWNER_ID)).stream()
			.filter(p -> p.getName().equals("newPet")).collect(Collectors.toList());

		assertNotNull(pets.get(0));

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(7)
	public void TestSavePetNegativeBadBirthDate() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());
		PetType type = petTypeService.findAvailable().iterator().next();

		Pet newPet = new Pet();
		LocalDate date = LocalDate.of(2021, 8, 11);
		newPet.setBirthDate(date);
		newPet.setName("newPet");
		newPet.setOwner(owner);
		newPet.setType(type);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.rejectValue("birthDate", "birthDateNotNull", "is required");
		String view = petController.savePet(newPet, bindingResult, model);

		assertEquals(view, "pets/createOrUpdatePetForm");

		assertNotNull(model.get("pet"));
		assertEquals(((Pet) model.get("pet")).getName(), "newPet");
		assertEquals(((Pet) model.get("pet")).getType(), type);
		assertEquals(((Pet) model.get("pet")).getOwner(), owner);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(8)
	public void TestSavePetNegativeBadName() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());
		PetType type = petTypeService.findAvailable().iterator().next();

		Pet newPet = new Pet();
		LocalDate date = LocalDate.of(2019, 8, 11);
		newPet.setBirthDate(date);
		newPet.setOwner(owner);
		newPet.setType(type);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.reject("name", "el tamaño tiene que estar entre 3 y 50");
		String view = petController.savePet(newPet, bindingResult, model);

		assertEquals(view, "pets/createOrUpdatePetForm");

		assertNotNull(model.get("pet"));
		assertEquals(((Pet) model.get("pet")).getBirthDate(), date);
		assertEquals(((Pet) model.get("pet")).getType(), type);
		assertEquals(((Pet) model.get("pet")).getOwner(), owner);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(9)
	public void TestSavePetNegativeBadType() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findByOwnerByUsername(SessionUtils.obtainUserInSession().getUsername());

		Pet newPet = new Pet();
		LocalDate date = LocalDate.of(2019, 8, 11);
		newPet.setBirthDate(date);
		newPet.setOwner(owner);
		newPet.setName("newPet");
		newPet.setType(null);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		bindingResult.rejectValue("type", "petTypeNotNull", "is required");
		String view = petController.savePet(newPet, bindingResult, model);

		assertEquals(view, "pets/createOrUpdatePetForm");

		assertNotNull(model.get("pet"));
		assertEquals(((Pet) model.get("pet")).getBirthDate(), date);
		assertEquals(((Pet) model.get("pet")).getName(), "newPet");
		assertEquals(((Pet) model.get("pet")).getOwner(), owner);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(10)
	public void TestSavePetNegativeBadOwner() {
		ModelMap model = new ModelMap();

		Owner owner = ownerService.findEntityById(TEST_OWNER_ID_2).get();
		PetType type = petTypeService.findAvailable().iterator().next();

		Pet newPet = new Pet();
		LocalDate date = LocalDate.of(2019, 8, 11);
		newPet.setBirthDate(date);
		newPet.setOwner(owner);
		newPet.setName("newPet");
		newPet.setType(type);

		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.savePet(newPet, bindingResult, model);

		assertEquals(view, "redirect:/oups");

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(11)
	public void TestDeletePetPositive() {
		ModelMap model = new ModelMap();
		String view = petController.deletePet(TEST_PET_ID, model);
		Optional<Pet> empty = Optional.empty();

		assertEquals(empty, petService.findEntityById(TEST_PET_ID));
		assertEquals(view, "redirect:/pets/listMyPets");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(12)
	public void TestDeletePetNegative() {
		ModelMap model = new ModelMap();
		String view = petController.deletePet(TEST_PET_ID_2, model);

		assertEquals("No estás autorizado", model.get("nonAuthorized"));
		assertNotNull(petService.findEntityById(TEST_PET_ID));
		assertEquals(view, "redirect:/pets/listMyPets");
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(2)
	public void TestNewVisitPositive() {
		ModelMap model = new ModelMap();
		String view = petController.newVisit(TEST_PET_ID, model);

		Owner owner = ownerService.findEntityById(TEST_OWNER_ID).get();
		assertEquals(owner.getClinic().getId(), model.get("clinicId"));
		assertNotNull(model.get("visit"));
		assertNotNull(model.get("visits"));
		assertEquals("visits/createOrUpdateVisitForm", view);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(3)
	public void TestNewStayPositive() {
		ModelMap model = new ModelMap();
		String view = petController.newStay(TEST_PET_ID, model);

		Owner owner = ownerService.findEntityById(TEST_OWNER_ID).get();
		assertEquals(owner.getClinic().getId(), model.get("clinicId"));
		assertNotNull(model.get("stay"));
		assertNotNull(model.get("stays"));
		assertEquals("stays/createOrUpdateStayForm", view);

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	@Order(4)
	public void TestInitUpdateForm() {
		ModelMap model = new ModelMap();
		String view = petController.initUpdateForm(TEST_PET_ID, model);

		Pet pet = petService.findEntityById(TEST_PET_ID).get();
		assertEquals(pet.getName(), ((Pet) model.get("pet")).getName());
		assertEquals("pets/createOrUpdatePetForm", view);
	}

}
