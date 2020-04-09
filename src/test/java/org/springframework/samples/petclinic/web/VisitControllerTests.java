
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = VisitController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VisitControllerTests {

	private static final int	TEST_PET_ID		= 1;

	private static final int	TEST_VISIT_ID	= 1;

	@MockBean
	private VisitService		visitService;

	@MockBean
	private VetService			vetService;


	//listAllPending (todas las visits devueltas tienen que tener isAcepted a null)
	@WithMockUser(value = "spring")
	@Test
	void testShowVisitsPending() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllPending")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
			.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	//listAllAccepted (todas las visits devueltas tienen que tener isAcepted a true)
	@WithMockUser(value = "spring")
	@Test
	void testShowVisitsAccept() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
			.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	//acceptVisit (pasarle una visit con isAccepted a null y te la actualice a true)
	@WithMockUser(value = "spring")
	@Test
	void testAcceptVisit() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", VisitControllerTests.TEST_VISIT_ID)).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/visits/listAllAccepted"));
	}

	//cancelVisit (pasarle una visit con isAccepted a null y te la actualice a false)
	@WithMockUser(value = "spring")
	@Test
	void testCancelVisit() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", VisitControllerTests.TEST_VISIT_ID)).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/visits/listAllAccepted"));
	}

	//changeDateVisit (la visit que entra es la misma que sale)
	@WithMockUser(value = "spring")
	@Test
	void testChangeDateVisit() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", VisitControllerTests.TEST_VISIT_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/visits/createOrUpdateVisitForm"));
	}

	//updateVisit (actualizar parametros (startDate, finishDate y description) y comprobar que se ha guardado bien)
	//date tiene que ser mínimo, dentro de 2 dias
	@WithMockUser(value = "spring")
	@Test
	void testVisitSuccesfull() throws Exception {
		this.mockMvc
			.perform(
				MockMvcRequestBuilders.post("/visits/save/{visitId}", VisitControllerTests.TEST_VISIT_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description").param("startDate", "2020-06-09").param("endDate", "2020-07-09"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/visits/createOrUpdateVisitForm"));
	}


	@MockBean
	private PetService	clinicService;

	@Autowired
	private MockMvc		mockMvc;


	@BeforeEach
	void setup() {

		LocalDate actualDate = LocalDate.now();

		Visit visit = new Visit();

		visit.setDescription("Descripcion");
		visit.setDate(actualDate);
		visit.setIsAccepted(null);

		Optional<Visit> optionalStay = Optional.of(visit);

		BDDMockito.given(this.visitService.findById(VisitControllerTests.TEST_VISIT_ID)).willReturn(optionalStay);

		BDDMockito.given(this.clinicService.findPetById(VisitControllerTests.TEST_PET_ID).get()).willReturn(new Pet());
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testInitNewVisitForm() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits/new", VisitControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessNewVisitFormSuccess() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", VisitControllerTests.TEST_PET_ID).param("name", "George").with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Visit Description"))
	//			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testProcessNewVisitFormHasErrors() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", VisitControllerTests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "George"))
	//			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visit")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowVisits() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits", VisitControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
	//			.andExpect(MockMvcResultMatchers.view().name("visitList"));
	//	}

}
