
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
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = StayController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class StayControllerTests {

	private static final int	TEST_STAY_ID	= 1;

	private static final int	TEST_VET_ID		= 1;

	private static final int	TEST_CLINIC_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private StayService			stayService;

	@MockBean
	private VetService			vetService;


	@BeforeEach
	void setup() {
		LocalDate actualDate = LocalDate.now();

		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC_ID);

		Stay stay = new Stay();
		stay.setDescription("Description");
		stay.setIsAccepted(null);
		stay.setStartDate(actualDate);
		stay.setFinishDate(actualDate);
		stay.setClinic(clinic);

		Optional<Stay> optionalStay = Optional.of(stay);

		BDDMockito.given(stayService.findEntityById(StayControllerTests.TEST_STAY_ID)).willReturn(optionalStay);

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

		BDDMockito.given(vetService.findByVetByUsername("pepito")).willReturn(pepe);

	}

	//listAllPending (todas las stays devueltas tienen que tener isAcepted a null)
	@WithMockUser(value = "spring")
	@Test
	void testShowStaysPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("stays"))
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	//listAllAccepted (todas las stays devueltas tienen que tener isAcepted a true)
	@WithMockUser(value = "spring")
	@Test
	void testShowStaysAccept() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("stays"))
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	//acceptStay (pasarle una stay con isAccepted a null y te la actualice a true)
	@WithMockUser(value = "pepito")
	@Test
	void testAcceptStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	//cancelStay (pasarle una stay con isAccepted a null y te la actualice a false)
	@WithMockUser(value = "pepito")
	@Test
	void testCancelStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	//changeDateStay (la stay que entra es la misma que sale)
	@WithMockUser(value = "spring")
	@Test
	void testChangeDateStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", StayControllerTests.TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/stays/createOrUpdateStayForm"));
	}

	//updateStay (actualizar parametros (startDate, finishDate y description) y comprobar que se ha guardado bien)
	//startDate tiene que estar en futuro
	//diferencia entre startdate y finishDate minimo de un dia, maximo siete
	@WithMockUser(value = "pepito")
	@Test
	void testStaySuccesfull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", StayControllerTests.TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

}
