
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
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

@WebMvcTest(value = SuggestionAdminController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitControllerTests {

	private static final int TEST_VET_ID = 1;

	private static final int TEST_VISIT_ID = 1;

	private static final int TEST_CLINIC_ID = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private VisitService		visitService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private AuthoritiesService	authoritiesService;

	@BeforeEach
	void setup() {

		LocalDateTime actualDate = LocalDateTime.now();

		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC_ID);

		Visit visit = new Visit();
		visit.setDescription("Descripcion");
		visit.setDateTime(actualDate);
		visit.setIsAccepted(null);
		visit.setClinic(clinic);

		Optional<Visit> optionalStay = Optional.of(visit);

		BDDMockito.given(visitService.findEntityById(VisitControllerTests.TEST_VISIT_ID)).willReturn(optionalStay);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("Vet");
		authority.setUsername("pepito");
		Vet pepe = new Vet();
		pepe.setUser(user);
		pepe.setId(TEST_VET_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");
		pepe.setClinic(clinic);

		BDDMockito.given(vetService.findPersonByUsername("pepito")).willReturn(pepe);

	}

	
	//listAllPending
	
	//listAllAccepted
	
	//acceptVisit
	
	//cancelVisit
	
	//initUpdateVisit
	
	//updateVisit
	
	
	
	//createVisit
	
	//listAllPendingAndAcceptedByOwner
	
	
/*
	// listAllPending (todas las visits devueltas tienen que tener isAcepted a null)
	@WithMockUser(value = "spring")
	@Test
	void testShowVisitsPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllPending"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
				.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	// listAllAccepted (todas las visits devueltas tienen que tener isAcepted a
	// true)
	@WithMockUser(value = "spring")
	@Test
	void testShowVisitsAccept() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
				.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	// acceptVisit (pasarle una visit con isAccepted a null y te la actualice a
	// true)
	@WithMockUser(value = "pepito")
	@Test
	void testAcceptVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", VisitControllerTests.TEST_VISIT_ID))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/visits/listAllAccepted"));
	}

	// cancelVisit (pasarle una visit con isAccepted a null y te la actualice a
	// false)
	@WithMockUser(value = "pepito")
	@Test
	void testCancelVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", VisitControllerTests.TEST_VISIT_ID))
				.andExpect(MockMvcResultMatchers.status().isFound())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/visits/listAllAccepted"));
	}

	// changeDateVisit (la visit que entra es la misma que sale)
	@WithMockUser(value = "spring")
	@Test
	void testChangeDateVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", VisitControllerTests.TEST_VISIT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/visits/createOrUpdateVisitForm"));
	}

	// updateVisit (actualizar parametros (startDate, finishDate y description) y
	// comprobar que se ha guardado bien)
	// date tiene que ser mínimo, dentro de 2 dias
	@WithMockUser(value = "pepito")
	@Test
	void testVisitSuccesfull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/visits/save/{visitId}", VisitControllerTests.TEST_VISIT_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("date", "2020/06/09")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}
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
