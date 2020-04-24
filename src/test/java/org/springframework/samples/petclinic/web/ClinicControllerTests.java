
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
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.StayService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ClinicController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class ClinicControllerTests {

	private static final int	TEST_VET_ID		= 1;

	private static final int	TEST_CLINIC_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private VetService			vetService;

	@MockBean
	private ClinicService		clinicService;

	@MockBean
	private OwnerService		ownerService;
	
	@MockBean
	private StayService			stayService;
	
	@MockBean
	private VisitService		visitService;


	@BeforeEach
	void setup() {
		LocalDate actualDate = LocalDate.now();

		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC_ID);

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

		Optional<Vet> optVet = Optional.of(pepe);
		BDDMockito.given(vetService.findEntityById(TEST_VET_ID)).willReturn(optVet);

	}

	//Show Clinic from a Vet
	@WithMockUser(value = "pepito")
	@Test
	void testShowClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("clinics/show"));
	}

}
