
package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.internal.Iterables;
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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = VisitController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_CLINIC_ID_1 = 1;

	private static final int TEST_CLINIC_ID_2 = 2;

	private static final int TEST_VET_ID = 1;

	private static final int TEST_PENDING_VISIT_ID = 1;

	private static final int TEST_PENDING_VISIT_ID_NOT_AUTHORIZED = 3;

	private static final int TEST_ACCEPTED_VISIT_ID = 2;

	private static final LocalDateTime TEST_DATE_AVAILABLE = LocalDateTime.of(LocalDate.now().plusMonths(3L),
			LocalTime.now());

	private static final LocalDateTime TEST_DATE_UNAVAILABLE = LocalDateTime.of(LocalDate.now().plusMonths(2L),
			LocalTime.now());

	private static final int TEST_PET_ID_1 = 1;
	
	private static final int TEST_PET_ID_2 = 2;

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VisitService visitService;

	@MockBean
	private VetService vetService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@BeforeEach
	void setup() {

		Clinic clinic1 = new Clinic();
		clinic1.setId(TEST_CLINIC_ID_1);
		clinic1.setAddress("Address " + TEST_CLINIC_ID_1);
		clinic1.setCity("City " + TEST_CLINIC_ID_1);
		clinic1.setName("Name " + TEST_CLINIC_ID_1);
		clinic1.setTelephone("65432100" + TEST_CLINIC_ID_1);

		Clinic clinic2 = new Clinic();
		clinic2.setId(TEST_CLINIC_ID_2);
		clinic2.setAddress("Address " + TEST_CLINIC_ID_2);
		clinic2.setCity("City " + TEST_CLINIC_ID_2);
		clinic2.setName("Name " + TEST_CLINIC_ID_2);
		clinic2.setTelephone("65432100" + TEST_CLINIC_ID_2);

		User userVet = new User();
		userVet.setEnabled(true);
		userVet.setUsername("Vet");
		userVet.setPassword("Password");

		Authorities authorityVet = new Authorities();
		authorityVet.setAuthority("veterinarian");
		authorityVet.setUsername("Vet");

		Vet vet = new Vet();
		vet.setUser(userVet);
		vet.setId(TEST_VET_ID);
		vet.setFirstName("Vet");
		vet.setLastName("User");
		vet.setAddress("Calle calle 1");
		vet.setCity("Ciudad");
		vet.setTelephone("654321000");
		vet.setClinic(clinic1);

		BDDMockito.given(vetService.findPersonByUsername("Vet")).willReturn(vet);

		User userOwner = new User();
		userOwner.setEnabled(true);
		userOwner.setUsername("Owner");
		userOwner.setPassword("Password");

		Authorities authorityOwner = new Authorities();
		authorityOwner.setAuthority("Owner");
		authorityOwner.setUsername("Owner");

		Owner owner = new Owner();
		owner.setUser(userOwner);
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("Owner");
		owner.setLastName("User");
		owner.setAddress("Calle calle 1");
		owner.setCity("Ciudad");
		owner.setTelephone("654321000");
		owner.setClinic(clinic1);

		BDDMockito.given(ownerService.findPersonByUsername("Owner")).willReturn(owner);

		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("dog");
		dog.setAvailable(true);

		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID_1);
		pet1.setName("toby");
		pet1.setOwner(owner);
		pet1.setType(dog);
		pet1.setBirthDate(LocalDate.of(2019, 7, 15));
		
		Pet pet2 = new Pet();
		pet2.setId(TEST_PET_ID_2);
		pet2.setName("toby");
		pet2.setOwner(owner);
		pet2.setType(dog);
		pet2.setBirthDate(LocalDate.of(2019, 7, 15));

		Visit pendingVisit = new Visit();
		pendingVisit.setId(TEST_PENDING_VISIT_ID);
		pendingVisit.setClinic(clinic1);
		pendingVisit.setDescription("Description pending visit");
		pendingVisit.setIsAccepted(null);
		pendingVisit.setDateTime(LocalDateTime.now().plusMonths(2L));
		pendingVisit.setPet(pet1);

		Optional<Visit> optionalPendingVisit = Optional.of(pendingVisit);
		BDDMockito.given(visitService.findEntityById(TEST_PENDING_VISIT_ID)).willReturn(optionalPendingVisit);

		BDDMockito.given(visitService.findAllPendingByVetId(TEST_VET_ID)).willReturn(Lists.newArrayList(pendingVisit));

		Visit pendingVisitNotAuthorized = new Visit();
		pendingVisitNotAuthorized.setId(TEST_PENDING_VISIT_ID_NOT_AUTHORIZED);
		pendingVisitNotAuthorized.setClinic(clinic2);
		pendingVisitNotAuthorized.setDescription("Description pending visit");
		pendingVisitNotAuthorized.setIsAccepted(null);
		pendingVisitNotAuthorized.setDateTime(LocalDateTime.now().plusMonths(2L));
		pendingVisitNotAuthorized.setPet(pet2);

		Optional<Visit> optionalPendingVisitNotAuthorized = Optional.of(pendingVisitNotAuthorized);
		BDDMockito.given(visitService.findEntityById(TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
				.willReturn(optionalPendingVisitNotAuthorized);

		Visit acceptedVisit = new Visit();
		acceptedVisit.setId(TEST_ACCEPTED_VISIT_ID);
		acceptedVisit.setClinic(clinic1);
		acceptedVisit.setDescription("Description accepted visit");
		acceptedVisit.setIsAccepted(true);
		acceptedVisit.setDateTime(LocalDateTime.now().plusMonths(2L));
		acceptedVisit.setPet(pet1);

		Optional<Visit> optionalAcceptedVisit = Optional.of(acceptedVisit);
		BDDMockito.given(visitService.findEntityById(TEST_ACCEPTED_VISIT_ID)).willReturn(optionalAcceptedVisit);

		BDDMockito.given(visitService.findAllAcceptedByVetId(TEST_VET_ID))
				.willReturn(Lists.newArrayList(acceptedVisit));

		BDDMockito.given(visitService.findAllByDateTime(TEST_DATE_AVAILABLE)).willReturn(Lists.newArrayList());

		BDDMockito.given(visitService.findAllByDateTime(TEST_DATE_UNAVAILABLE))
				.willReturn(Lists.newArrayList(acceptedVisit));

		BDDMockito.given(vetService.findVetsByClinicId(TEST_CLINIC_ID_1)).willReturn(Lists.newArrayList(vet));

		BDDMockito.given(visitService.findAllByPetId(TEST_PET_ID_1))
				.willReturn(Lists.newArrayList(acceptedVisit, pendingVisit));

		BDDMockito.given(visitService.findAllAcceptedByOwnerId(TEST_OWNER_ID))
				.willReturn(Lists.newArrayList(acceptedVisit));

		BDDMockito.given(visitService.findAllPendingByOwnerId(TEST_OWNER_ID))
				.willReturn(Lists.newArrayList(pendingVisit));
		
		BDDMockito.given(authoritiesService.findAuthorityByUsername("Owner"))
		.willReturn("owner");
		
		BDDMockito.given(authoritiesService.findAuthorityByUsername("Vet"))
		.willReturn("veterinarian");

		// BDDMockito.given(visitService.saveEntity(acceptedVisit)).willReturn(acceptedVisit);

	}

	@WithMockUser(value = "Vet")
	@Test
	void testListAllPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllPending"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
				.andExpect(MockMvcResultMatchers.model().attribute("accepted", false))
				.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	@WithMockUser(value = "Vet")
	@Test
	void testListAllAccepted() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
				.andExpect(MockMvcResultMatchers.model().attribute("accepted", true))
				.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	// TODO:
	@WithMockUser(value = "Vet")
	@Test
	void testAcceptVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_PENDING_VISIT_ID))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listAllAccepted"));
	}

	// TODO:
	@WithMockUser(value = "Vet")
	@Test
	void testAcceptVisitNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
				.andExpect(status().is3xxRedirection())
				// .andExpect(MockMvcResultMatchers.model().attributeExists("nonAuthorized"))
				.andExpect(view().name("redirect:/visits/listAllAccepted"));
	}

	// TODO:
	@WithMockUser(value = "Vet")
	@Test
	void testCancelVisitAsVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listAllAccepted"));
	}
	
	// TODO:
	@WithMockUser(value = "Vet")
	@Test
	void testCancelVisitAsVetNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
				// .andExpect(MockMvcResultMatchers.model().attributeExists("nonAuthorized"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listAllAccepted"));
	}

	// TODO:
	@WithMockUser(value = "Owner")
	@Test
	void testCancelVisitAsOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listByOwner"));
	}
	
	// TODO:
	@WithMockUser(value = "Owner")
	@Test
	void testCancelVisitAsOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
		// .andExpect(MockMvcResultMatchers.model().attributeExists("nonAuthorized"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listByOwner"));
	}

	@WithMockUser(value = "Vet")
	@Test
	void testInitUpdateVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", TEST_PENDING_VISIT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visit"))
				.andExpect(MockMvcResultMatchers.view().name("/visits/createOrUpdateVisitForm"));
	}

	// updateVisit

	// createVisit

	@WithMockUser(value = "Owner")
	@Test
	void testListAllPendingAndAcceptedByOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listByOwner"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visitsPending"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("visitsAccepted"))
				.andExpect(MockMvcResultMatchers.view().name("visits/listByOwner"));
	}

	/*
	 *
	 * 
	 * // listAllAccepted (todas las visits devueltas tienen que tener isAcepted a
	 * // true)
	 * 
	 * @WithMockUser(value = "spring")
	 * 
	 * @Test void testShowVisitsAccept() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted"))
	 * .andExpect(MockMvcResultMatchers.status().isOk())
	 * .andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
	 * .andExpect(MockMvcResultMatchers.view().name("visits/list")); }
	 * 
	 * // acceptVisit (pasarle una visit con isAccepted a null y te la actualice a
	 * // true)
	 * 
	 * @WithMockUser(value = "pepito")
	 * 
	 * @Test void testAcceptVisit() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}",
	 * VisitControllerTests.TEST_VISIT_ID))
	 * .andExpect(MockMvcResultMatchers.status().isFound())
	 * .andExpect(MockMvcResultMatchers.view().name(
	 * "redirect:/visits/listAllAccepted")); }
	 * 
	 * // cancelVisit (pasarle una visit con isAccepted a null y te la actualice a
	 * // false)
	 * 
	 * @WithMockUser(value = "pepito")
	 * 
	 * @Test void testCancelVisit() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}",
	 * VisitControllerTests.TEST_VISIT_ID))
	 * .andExpect(MockMvcResultMatchers.status().isFound())
	 * .andExpect(MockMvcResultMatchers.view().name(
	 * "redirect:/visits/listAllAccepted")); }
	 * 
	 * // changeDateVisit (la visit que entra es la misma que sale)
	 * 
	 * @WithMockUser(value = "spring")
	 * 
	 * @Test void testChangeDateVisit() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}",
	 * VisitControllerTests.TEST_VISIT_ID))
	 * .andExpect(MockMvcResultMatchers.status().isOk())
	 * .andExpect(MockMvcResultMatchers.view().name(
	 * "/visits/createOrUpdateVisitForm")); }
	 * 
	 * // updateVisit (actualizar parametros (startDate, finishDate y description) y
	 * // comprobar que se ha guardado bien) // date tiene que ser mínimo, dentro de
	 * 2 dias
	 * 
	 * @WithMockUser(value = "pepito")
	 * 
	 * @Test void testVisitSuccesfull() throws Exception {
	 * mockMvc.perform(MockMvcRequestBuilders.post("/visits/save/{visitId}",
	 * VisitControllerTests.TEST_VISIT_ID)
	 * .with(SecurityMockMvcRequestPostProcessors.csrf()).param("description",
	 * "Description") .param("date",
	 * "2020/06/09")).andExpect(MockMvcResultMatchers.status().isOk())
	 * .andExpect(MockMvcResultMatchers.view().name("visits/list")); }
	 */
	// @WithMockUser(value = "spring")
	// @Test
	// void testInitNewVisitForm() throws Exception {
	// this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits/new",
	// VisitControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	// }
	//
	// @WithMockUser(value = "spring")
	// @Test
	// void testProcessNewVisitFormSuccess() throws Exception {
	// this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new",
	// VisitControllerTests.TEST_PET_ID).param("name",
	// "George").with(SecurityMockMvcRequestPostProcessors.csrf()).param("description",
	// "Visit Description"))
	// .andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	// }
	//
	// @WithMockUser(value = "spring")
	// @Test
	// void testProcessNewVisitFormHasErrors() throws Exception {
	// this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new",
	// VisitControllerTests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name",
	// "George"))
	// .andExpect(MockMvcResultMatchers.model().attributeHasErrors("visit")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	// }
	//
	// @WithMockUser(value = "spring")
	// @Test
	// void testShowVisits() throws Exception {
	// this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits",
	// VisitControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
	// .andExpect(MockMvcResultMatchers.view().name("visitList"));
	// }

}
