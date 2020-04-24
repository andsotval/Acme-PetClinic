
package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.samples.petclinic.model.Manager;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	private static final int	TEST_VET_ID		= 1;

	private static final int	TEST_MANAGER_ID	= 3;

	@MockBean
	private ClinicService		clinicService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private ManagerService		managerService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setId(TEST_VET_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		Optional<Vet> opt = Optional.of(james);
		BDDMockito.given(vetService.findEntityById(TEST_VET_ID)).willReturn(opt);

		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setAddress("110 W. Liberty St.");
		helen.setCity("Madison");
		helen.setTelephone("6085551023");
		helen.setId(2);

		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		james.addSpecialty(radiology);
		BDDMockito.given(vetService.findAllEntities()).willReturn(Lists.newArrayList(james, helen));

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("manager");
		authority.setUsername("pepito");
		Manager pepe = new Manager();
		pepe.setUser(user);
		pepe.setId(TEST_MANAGER_ID);
		pepe.setFirstName("Pepe");
		pepe.setLastName("Leary");
		pepe.setAddress("110 W. Liberty St.");
		pepe.setCity("Madison");
		pepe.setTelephone("6085551023");

		BDDMockito.given(managerService.findPersonByUsername("pepito")).willReturn(pepe);

		Clinic clinic = new Clinic();
		clinic.setId(1);
		clinic.setCity("Sevilla");
		BDDMockito.given(clinicService.findClinicByManagerId(TEST_MANAGER_ID)).willReturn(clinic);

	}

	@WithMockUser(value = "pepito")
	@Test
	void testVetsAvailableAndOwnList() throws Exception {
		mockMvc.perform(get("/vets/vetsAvailable")).andExpect(status().isOk())
			.andExpect(model().attributeExists("vets2")).andExpect(model().attributeExists("hiredVets"))
			.andExpect(view().name("vets/vetsAvailable"));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testAcceptVet() throws Exception {
		mockMvc.perform(get("/vets/accept/{vetId}", TEST_VET_ID)).andExpect(status().isFound())
			.andExpect(view().name("redirect:/vets/vetsAvailable"));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testShowVet() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("vet")).andExpect(view().name("vets/vetDetails"));
	}

}
