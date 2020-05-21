/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 17-may-2020
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(controllers = StayController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class StayControllerTests {

	private static final int	TEST_STAY_ID	= 1;
	private static final int	TEST_STAY_99_ID	= 99;

	private static final int	TEST_VET1_ID	= 1;
	private static final int	TEST_VET2_ID	= 2;

	private static final int	TEST_OWNER_ID	= 1;
	private static final int	TEST_OWNER_2_ID	= 2;

	private static final int	TEST_CLINIC1_ID	= 1;
	private static final int	TEST_CLINIC2_ID	= 2;

	private static final int	TEST_PET_ID		= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private StayService			stayService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private PetService			petService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private AuthoritiesService	authoritiesService;


	@BeforeEach
	void setup() {
		LocalDate actualDate = LocalDate.now();

		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC1_ID);

		Clinic clinic2 = new Clinic();
		clinic2.setId(TEST_CLINIC2_ID);

		User userOwner = new User();
		userOwner.setEnabled(true);
		userOwner.setUsername("owner");
		userOwner.setPassword("owner");

		Authorities authorityOwner = new Authorities();
		authorityOwner.setAuthority("Owner");
		authorityOwner.setUsername("Owner");

		Owner owner = new Owner();
		owner.setUser(userOwner);
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("Owner");
		owner.setLastName("Leary");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		owner.setClinic(clinic);

		User userOwner2 = new User();
		userOwner2.setEnabled(true);
		userOwner2.setUsername("owner2");
		userOwner2.setPassword("owner2");

		Authorities authorityOwner2 = new Authorities();
		authorityOwner2.setAuthority("Owner");
		authorityOwner2.setUsername("Owner2");

		Owner owner2 = new Owner();
		owner2.setUser(userOwner2);
		owner2.setId(TEST_OWNER_2_ID);
		owner2.setFirstName("Owner");
		owner2.setLastName("Leary");
		owner2.setAddress("110 W. Liberty St.");
		owner2.setCity("Madison");
		owner2.setTelephone("6085551023");
		owner2.setClinic(clinic2);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("vet1");
		user.setPassword("vet1");

		Authorities authority = new Authorities();
		authority.setAuthority("Vet");
		authority.setUsername("vet1");
		Vet pepe = new Vet();
		pepe.setUser(user);
		pepe.setId(TEST_VET1_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");
		pepe.setClinic(clinic);

		//FakeVet ------------------------------------
		User falseUser = new User();
		falseUser.setEnabled(true);
		falseUser.setUsername("falseVet");
		falseUser.setPassword("falseVet");

		Authorities falseAuthority = new Authorities();
		falseAuthority.setAuthority("Vet");
		falseAuthority.setUsername("falseVet");

		Vet falsePepe = new Vet();
		falsePepe.setUser(user);
		falsePepe.setId(TEST_VET2_ID);
		falsePepe.setFirstName("falseVet");
		falsePepe.setLastName("Leary");
		falsePepe.setAddress("110 W. Liberty St.");
		falsePepe.setCity("Madison");
		falsePepe.setTelephone("6085551023");
		falsePepe.setClinic(clinic2);
		//---------------------------------------------

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2019, Month.AUGUST, 10));
		pet.setId(TEST_PET_ID);
		pet.setName("Wiskers");
		pet.setOwner(owner);

		Stay stay = new Stay();
		stay.setDescription("Description");
		stay.setIsAccepted(null);
		stay.setStartDate(actualDate);
		stay.setFinishDate(actualDate);
		stay.setClinic(clinic);
		stay.setPet(pet);

		Collection<Stay> stays = new ArrayList<Stay>();
		stays.add(stay);

		Optional<Stay> optionalStay = Optional.of(stay);

		Optional<Owner> optionalOwner = Optional.of(owner);
		Optional<Owner> optionalOwner2 = Optional.of(owner2);

		BDDMockito.given(stayService.findEntityById(StayControllerTests.TEST_STAY_ID)).willReturn(optionalStay);
		BDDMockito.given(stayService.findAllAcceptedByVet(StayControllerTests.TEST_VET1_ID)).willReturn(stays);
		BDDMockito.given(stayService.findAllAcceptedByVet(StayControllerTests.TEST_VET2_ID)).willReturn(stays);

		BDDMockito.given(vetService.findPersonByUsername("vet1")).willReturn(pepe);
		BDDMockito.given(vetService.findPersonByUsername("falseVet")).willReturn(falsePepe);

		BDDMockito.given(authoritiesService.findAuthorityByUsername("vet1")).willReturn("veterinarian");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("falseVet")).willReturn("veterinarian");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("falseVet2")).willReturn("veterinarian");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("owner")).willReturn("owner");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("owner2")).willReturn("owner");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("falseOwner")).willReturn("owner");

		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID)).willReturn(optionalOwner);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_2_ID)).willReturn(optionalOwner2);
		BDDMockito.given(ownerService.findPersonByUsername(owner.getUser().getUsername())).willReturn(owner);
		BDDMockito.given(ownerService.findPersonByUsername(owner2.getUser().getUsername())).willReturn(owner2);
	}

	//listAllPending (todas las stays devueltas tienen que tener isAcepted a null)
	@WithMockUser(value = "vet1")
	@Test
	void testShowStaysPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			/* .andExpect(MockMvcResultMatchers.model().attributeExists("stays")) */.andExpect(
				MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "FalseVet")
	@Test
	void testShowStaysPendingNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//listAllAccepted (todas las stays devueltas tienen que tener isAcepted a true)
	@WithMockUser(value = "vet1")
	@Test
	void testShowStaysAccept() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			/* .andExpect(MockMvcResultMatchers.model().attributeExists("stays")) */.andExpect(
				MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "manager")
	@Test
	void testShowStaysAcceptNegativeUserNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	//acceptStay (pasarle una stay con isAccepted a null y te la actualice a true)
	@WithMockUser(value = "vet1")
	@Test
	void testAcceptStayPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "falseVet")
	@Test
	void testAcceptStayNegativeNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status()
				.isFound())/* .andExpect(MockMvcResultMatchers.model().attributeExists("nonAuthorized")) */
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testAcceptStayNegativeNotExistingStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", StayControllerTests.TEST_STAY_99_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet")
	@Test
	void testAcceptStayNegativeAcceptStayOfAnotherClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//cancelStay (pasarle una stay con isAccepted a null y te la actualice a false)
	@WithMockUser(value = "vet1")
	@Test
	void testCancelStayAsVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testCancelStayNotExisting() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_99_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet")
	@Test
	void testCancelStayAsVetNegativeCancelStayOfOtherClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet2")
	@Test
	void testCancelStaytAsVetNegativeVeterinarianNotInSystem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testCancelStayAsOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listByOwner"));
	}

	@WithMockUser(value = "falseOwner")
	@Test
	void testCancelStaytAsOwnerNegativeOwnerNotInSystem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner2")
	@Test
	void testCancelStayAsOwnerNegativeCancelStayOfOtherClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testChangeDateStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "provider1")
	@Test
	void testChangeDateStayNotAuthorizedAsOtherUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", 13))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testChangeDateStayNegativeUnexistingStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", 99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet")
	@Test
	void testChangeDateStayNotAuthorizedAsVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//updateStay (actualizar parametros (startDate, finishDate y description) y comprobar que se ha guardado bien)
	//startDate tiene que estar en futuro
	//diferencia entre startdate y finishDate minimo de un dia, maximo siete
	@WithMockUser(value = "vet1")
	@Test
	void testUpdateStaySuccesfull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", StayControllerTests.TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testUpdateStayNegativeUnexistingStay() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_99_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet2")
	@Test
	void testUpdateStayNegativeVetNotInSystem() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "falseVet")
	@Test
	void testUpdateStayNegativeNotAuthorizedAsVet() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testSuccesfullStayNegativeNoLongerThan7Days() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", StayControllerTests.TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "2020/05/22").param("finishDate", "2020/06/23")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateMinimumOneWeek"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testSuccesfullStayNegativeDateNotNull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", StayControllerTests.TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "").param("finishDate", "2020/05/22")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1")
	@Test
	void testSuccesfullStayNegativeFinishDateBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", StayControllerTests.TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "2021/11/01").param("finishDate", "2021/10/01")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateAfterStartDate"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testSuccesfullStayNegativeLessThan2Days() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1L);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue())
			+ "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue())
			+ "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", startDate)
				.param("finishDate", finishDate).param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	// /save
	@WithMockUser(value = "owner")
	@Test
	void testNewStayPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2020/05/22")
				.param("finishDate", "2020/05/29").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			/* .andExpect(MockMvcResultMatchers.model().attribute("message", "Stay succesfully updated")) */.andExpect(
				MockMvcResultMatchers.view().name("redirect:/stays/listByOwner"));
	}

	@WithMockUser(value = "falseOwner")
	@Test
	void testNewStayNegativeNotAuthorized() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2020/05/22")
				.param("finishDate", "2020/05/29").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			/* .andExpect(MockMvcResultMatchers.model().attribute("message", "Stay succesfully updated")) */.andExpect(
				MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testNewStayNegativeNoLongerThan7Days() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2020/05/22")
				.param("finishDate", "2020/06/23").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateMinimumOneWeek"))
			/* .andExpect(MockMvcResultMatchers.model().attribute("message", "Stay succesfully updated")) */.andExpect(
				MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testNewStayNegativeDateNotNull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "").param("finishDate", "")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testNewStayNegativeFinishBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2021/11/01")
				.param("finishDate", "2021/10/01").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateAfterStartDate"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner")
	@Test
	void testNewStayNegativeLessThan2Days() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue())
			+ "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue())
			+ "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", startDate)
				.param("finishDate", finishDate).param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	// ListByOwner
	@WithMockUser(value = "owner")
	@Test
	void ListAllPendingByOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			/*
			 * .andExpect(MockMvcResultMatchers.model().attributeExists("staysPending")) * .andExpect(MockMvcResultMatchers.model().attributeExists("staysAccepted"))
			 */.andExpect(MockMvcResultMatchers.view().name("stays/listByOwner"));
	}

	@WithMockUser(value = "otherUser")
	@Test
	void ListAllPendingByOwnerNegativeNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
}
