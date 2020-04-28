
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
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

@WebMvcTest(controllers = ClinicController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ClinicControllerTests {

	private static final int	TEST_VET_ID		= 1;
	
	private static final int	TEST_OWNER_ID_1	= 1;
	
	private static final int	TEST_OWNER_ID_2	= 2;

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

		User userOwner = new User();
		userOwner.setEnabled(true);
		userOwner.setUsername("joselito");
		userOwner.setPassword("joselito");
		
		Authorities authorityOwner = new Authorities();
		authorityOwner.setAuthority("Owner");
		authorityOwner.setUsername("joselito");
		Owner joselito = new Owner();
		joselito.setUser(userOwner);
		joselito.setId(TEST_OWNER_ID_1);
		joselito.setFirstName("Joselito");
		joselito.setLastName("Leary");
		joselito.setAddress("110 W. Liberty St.");
		joselito.setCity("Madison");
		joselito.setTelephone("6085551023");
		joselito.setClinic(clinic);
		
		User userOwner2 = new User();
		userOwner2.setEnabled(true);
		userOwner2.setUsername("manolito");
		userOwner2.setPassword("manolito");
		
		Authorities authorityOwner2 = new Authorities();
		authorityOwner2.setAuthority("Owner");
		authorityOwner2.setUsername("manolito");
		Owner manolito = new Owner();
		manolito.setUser(userOwner2);
		manolito.setId(TEST_OWNER_ID_2);
		manolito.setFirstName("Manolito");
		manolito.setLastName("Leary");
		manolito.setAddress("110 W. Liberty St.");
		manolito.setCity("Madison");
		manolito.setTelephone("6085551023");
		manolito.setClinic(null);
		
		List<Visit> visitVacia = new ArrayList<Visit>();
		List<Stay> stayVacia = new ArrayList<Stay>();

		
		BDDMockito.given(vetService.findPersonByUsername("pepito")).willReturn(pepe);
		
		BDDMockito.given(ownerService.findPersonByUsername("joselito")).willReturn(joselito);
		
		BDDMockito.given(ownerService.findPersonByUsername("manolito")).willReturn(manolito);
		
		BDDMockito.given(visitService.findAllAcceptedByOwner(joselito)).willReturn(visitVacia);
		
		BDDMockito.given(visitService.findAllPendingByOwner(joselito)).willReturn(visitVacia);

		BDDMockito.given(stayService.findAllAcceptedByOwner(joselito)).willReturn(stayVacia);
		
		BDDMockito.given(stayService.findAllPendingByOwner(joselito)).willReturn(stayVacia);
		
		Optional<Vet> optVet = Optional.of(pepe);
		BDDMockito.given(vetService.findEntityById(TEST_VET_ID)).willReturn(optVet);
		Optional<Owner> optOwner = Optional.of(joselito);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_1)).willReturn(optOwner);
		Optional<Owner> optOwner2 = Optional.of(manolito);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_2)).willReturn(optOwner2);

	}

	//Show Clinic from a Vet
	@WithMockUser(value = "pepito")
	@Test
	void testShowClinicVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			//.andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}
	
	//Show Clinic from a Owner
	@WithMockUser(value = "joselito")
	@Test
	void testShowClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			//.andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}
	
	//Show Clinic from a Owner
	@WithMockUser(value = "manolito")
	@Test
	void testShowListClinicOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			//.andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}

}
