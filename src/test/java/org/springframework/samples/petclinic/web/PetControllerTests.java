
package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PetControllerTests {

	private static final int	TEST_OWNER_ID		= 1;
	private static final int	TEST_OWNER_ID_2		= 2;

	private static final int	TEST_PET_ID			= 1;
	private static final int	TEST_PET_ID_2		= 2;
	private static final int	TEST_PET_ID_WRONG	= 5;

	private static Pet			pet1;
	private static Pet			pet2;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private PetService			petService;

	@MockBean
	private PetTypeService		petTypeService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private VisitService		visitService;

	@MockBean
	private StayService			stayService;


	@BeforeEach
	void setup() {

		Clinic clinic = new Clinic();
		clinic.setId(1);
		clinic.setName("Clinic 1");
		clinic.setCity("City");
		clinic.setAddress("Address");
		clinic.setTelephone("642968344");

		User userOwner = new User();
		userOwner.setEnabled(true);
		userOwner.setId(TEST_OWNER_ID);
		userOwner.setUsername("George");
		userOwner.setPassword("George");

		Authorities authorityOwner = new Authorities();
		authorityOwner.setAuthority("owner");
		authorityOwner.setUsername("George");

		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setUser(userOwner);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setMail("owner@owner.com");
		owner.setTelephone("6085551023");
		owner.setClinic(clinic);

		Optional<Owner> optOwner = Optional.of(owner);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID)).willReturn(optOwner);
		BDDMockito.given(ownerService.findPersonByUsername("George")).willReturn(owner);

		User userOwner2 = new User();
		userOwner2.setEnabled(true);
		userOwner2.setId(TEST_OWNER_ID_2);
		userOwner2.setUsername("Lucas");
		userOwner2.setPassword("Lucas");

		Authorities authorityOwner2 = new Authorities();
		authorityOwner2.setAuthority("owner");
		authorityOwner2.setUsername("Lucas");

		Owner owner2 = new Owner();
		owner2.setId(TEST_OWNER_ID_2);
		owner2.setUser(userOwner);
		owner2.setFirstName("Lucas");
		owner2.setLastName("Smith");
		owner2.setAddress("110 W. Liberty St.");
		owner2.setCity("Madison");
		owner2.setMail("owner2@owner2.com");
		owner2.setTelephone("6085551789");

		Optional<Owner> optOwner2 = Optional.of(owner2);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_2)).willReturn(optOwner2);
		BDDMockito.given(ownerService.findPersonByUsername("Lucas")).willReturn(owner2);

		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");
		cat.setAvailable(true);

		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("dog");
		dog.setAvailable(true);

		PetType rabbit = new PetType();
		rabbit.setId(3);
		rabbit.setName("rabbit");
		rabbit.setAvailable(true);

		List<PetType> petTypeList = new ArrayList<PetType>();
		petTypeList.add(cat);
		petTypeList.add(dog);
		petTypeList.add(rabbit);

		BDDMockito.given(petTypeService.findAvailable()).willReturn(petTypeList);

		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID);
		pet1.setName("toby");
		pet1.setOwner(owner);
		pet1.setType(dog);
		LocalDate birthday = LocalDate.of(2019, 7, 15);
		pet1.setBirthDate(birthday);
		PetControllerTests.pet1 = pet1;

		Optional<Pet> toby = Optional.of(pet1);
		BDDMockito.given(petService.findEntityById(TEST_PET_ID)).willReturn(toby);

		Pet pet2 = new Pet();
		pet2.setId(TEST_PET_ID_2);
		pet2.setName("coco");
		pet2.setOwner(owner);
		pet2.setType(cat);
		LocalDate birthday2 = LocalDate.of(2019, 11, 29);
		pet2.setBirthDate(birthday2);
		PetControllerTests.pet2 = pet2;

		Optional<Pet> coco = Optional.of(pet2);
		BDDMockito.given(petService.findEntityById(TEST_PET_ID_2)).willReturn(coco);

		List<Pet> ownerPets = Lists.newArrayList(pet1, pet2);
		BDDMockito.given(petService.findPetsByOwnerId(TEST_OWNER_ID)).willReturn(ownerPets);
	}

	@WithMockUser(value = "George")
	@Test
	void testListMyPets() throws Exception {
		mockMvc.perform(get("/pets/listMyPets")).andExpect(status().isOk()).andExpect(model().attributeExists("pets")).andExpect(model().attributeExists("message")).andExpect(view().name("pets/list"));
	}

	@WithMockUser(value = "George")
	@Test
	void testSavePet() throws Exception {
		mockMvc.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11").param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID))).andExpect(status().isOk())
			/* .andExpect(model().attribute("message", "Stay succesfully updated")) */
			.andExpect(view().name("pets/list"));

	}

	@WithMockUser(value = "George")
	@Test
	void testSavePetWrongBirthDate() throws Exception {
		mockMvc.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2021/04/11").param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID))).andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "birthDateFuture")).andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "George")
	@Test
	void testSavePetWrongType() throws Exception {
		mockMvc.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11").param("type", "bird").param("owner.id", String.valueOf(TEST_OWNER_ID))).andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("pet", "type")).andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "George")
	@Test
	void testSavePetWrongName() throws Exception {
		mockMvc.perform(post("/pets/save").with(csrf()).param("name", "wn").param("birthDate", "2019/04/11").param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID))).andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("pet", "name")).andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "Lucas")
	@Test
	void testSavePetDeniedAccess() throws Exception {
		mockMvc.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11").param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID))).andExpect(status().is3xxRedirection())/*
																																																							 * .andExpect(model().
																																																							 * attributeExists("exception"))
																																																							 */
			.andExpect(view().name("redirect:/oups"));

	}

	@WithMockUser(value = "George")
	@Test
	void testDeleteVisit() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID)).andExpect(status().isOk()).andExpect(view().name("pets/list"));
	}

	@WithMockUser(value = "George")
	@Test
	void testDeletePetNotPresent() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "George")
	@Test
	void testNewVisit() throws Exception {
		mockMvc.perform(get("/pets/newVisit/{petId}", TEST_PET_ID_2)).andExpect(status().isOk()).andExpect(model().attributeExists("clinicId")).andExpect(model().attributeExists("visit")).andExpect(model().attributeExists("visits"))
			.andExpect(view().name("visits/createOrUpdateVisitForm"));
	}

	@WithMockUser(value = "George")
	@Test
	void testNewVisitWrongPetId() throws Exception {
		mockMvc.perform(get("/pets/newVisit/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection()).andExpect(model().attributeDoesNotExist("clinicId")).andExpect(model().attributeDoesNotExist("visit"))
			.andExpect(model().attributeDoesNotExist("visits")).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "George")
	@Test
	void testNewStay() throws Exception {
		mockMvc.perform(get("/pets/newStay/{petId}", TEST_PET_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("hasClinic")).andExpect(model().attributeExists("stay")).andExpect(model().attributeExists("stays"))
			.andExpect(view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "George")
	@Test
	void testNewStayWrongPetId() throws Exception {
		mockMvc.perform(get("/pets/newStay/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection()).andExpect(model().attributeDoesNotExist("clinicId")).andExpect(model().attributeDoesNotExist("stay"))
			.andExpect(model().attributeDoesNotExist("stays")).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "George")
	@Test
	void testInitUpdateFormPet1() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID)).andExpect(status().isOk()).andExpect(model().attributeExists("pet")).andExpect(model().attribute("pet", pet1)).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "George")
	@Test
	void testInitUpdateFormPet2() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID_2)).andExpect(status().isOk()).andExpect(model().attributeExists("pet")).andExpect(model().attribute("pet", pet2)).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "George")
	@Test
	void testInitUpdateFormPetIdWrong() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection()).andExpect(model().attributeDoesNotExist("pet")).andExpect(view().name("redirect:/oups"));
	}

	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testInitCreationForm() throws Exception {
	//		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID)).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testProcessCreationFormSuccess() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("type", "hamster")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//    @Test
	//	void testProcessCreationFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(model().attributeHasNoErrors("owner"))
	//				.andExpect(model().attributeHasErrors("pet"))
	//				.andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}
	//
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testInitUpdateForm() throws Exception {
	//		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
	//				.andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}
	//
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormSuccess() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("type", "hamster")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(model().attributeHasNoErrors("owner"))
	//				.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}

}
