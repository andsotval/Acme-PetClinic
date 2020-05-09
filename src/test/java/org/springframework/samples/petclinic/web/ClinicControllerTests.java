
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.service.AuthoritiesService;
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

	private static final int	TEST_VET_ID					= 1;

	private static final int	TEST_OWNER_ID_1				= 1;

	private static final int	TEST_OWNER_ID_2				= 2;

	private static final int	TEST_OWNER_ID_3				= 3;

	private static final int	TEST_CLINIC_ID				= 1;
	private static final int	TEST_CLINIC_2_ID			= 2;
	private static final int	TEST_NOT_EXISTING_CLINIC_ID	= 2;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ClinicService		clinicService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private VetService			vetService;

	@MockBean
	private StayService			stayService;

	@MockBean
	private VisitService		visitService;

	@MockBean
	private AuthoritiesService	authoritiesService;


	@BeforeEach
	void setup() {
		Clinic clinic = new Clinic();
		clinic.setId(TEST_CLINIC_ID);
		Optional<Clinic> optionalClinic = Optional.of(clinic);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authorityVet = new Authorities();
		authorityVet.setAuthority("Veterinarian");
		authorityVet.setUsername("pepito");

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
		authorityOwner.setAuthority("owner");
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

		//Owner With clinic and Stays and Visits booked
		User userOwner3 = new User();
		userOwner3.setEnabled(true);
		userOwner3.setUsername("manolito2");
		userOwner3.setPassword("manolito2");

		Authorities authorityOwner3 = new Authorities();
		authorityOwner3.setAuthority("Owner");
		authorityOwner3.setUsername("manolito");
		Owner manolito2 = new Owner();
		manolito2.setUser(userOwner3);
		manolito2.setId(TEST_OWNER_ID_3);
		manolito2.setFirstName("Manolito");
		manolito2.setLastName("Leary");
		manolito2.setAddress("110 W. Liberty St.");
		manolito2.setCity("Madison");
		manolito2.setTelephone("6085551023");
		manolito2.setClinic(clinic);

		List<Visit> visitVacia = new ArrayList<Visit>();
		List<Stay> stayVacia = new ArrayList<Stay>();

		BDDMockito.given(vetService.findPersonByUsername("pepito")).willReturn(pepe);

		BDDMockito.given(ownerService.findPersonByUsername("joselito")).willReturn(joselito);

		BDDMockito.given(ownerService.findPersonByUsername("manolito")).willReturn(manolito);

		BDDMockito.given(ownerService.findPersonByUsername("manolito2")).willReturn(manolito2);

		BDDMockito.given(visitService.findAllAcceptedByOwnerId(joselito.getId())).willReturn(visitVacia);

		BDDMockito.given(visitService.findAllPendingByOwnerId(joselito.getId())).willReturn(visitVacia);

		BDDMockito.given(visitService.canUnsubscribe(joselito.getId())).willReturn(true);

		BDDMockito.given(stayService.canUnsubscribe(joselito.getId())).willReturn(true);

		BDDMockito.given(visitService.canUnsubscribe(manolito2.getId())).willReturn(false);

		BDDMockito.given(visitService.canUnsubscribe(manolito2.getId())).willReturn(false);

		BDDMockito.given(stayService.findAllAcceptedByOwner(joselito.getId())).willReturn(stayVacia);

		BDDMockito.given(stayService.findAllPendingByOwner(joselito.getId())).willReturn(stayVacia);

		BDDMockito.given(clinicService.findEntityById(TEST_CLINIC_ID)).willReturn(optionalClinic);

		BDDMockito.given(authoritiesService.findAuthorityByUsername(authorityOwner.getUsername())).willReturn("owner");
		BDDMockito.given(authoritiesService.findAuthorityByUsername(authorityOwner2.getUsername())).willReturn("owner");
		BDDMockito.given(authoritiesService.findAuthorityByUsername(authorityOwner3.getUsername())).willReturn("owner");
		BDDMockito.given(authoritiesService.findAuthorityByUsername(authorityVet.getUsername())).willReturn("veterinarian");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("Falsepepito")).willReturn("veterinarian");
		BDDMockito.given(authoritiesService.findAuthorityByUsername("Falsejoselito")).willReturn("owner");

		Optional<Vet> optVet = Optional.of(pepe);
		BDDMockito.given(vetService.findEntityById(TEST_VET_ID)).willReturn(optVet);
		Optional<Owner> optOwner = Optional.of(joselito);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_1)).willReturn(optOwner);
		Optional<Owner> optOwner2 = Optional.of(manolito);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_2)).willReturn(optOwner2);
		Optional<Owner> optOwner3 = Optional.of(manolito2);
		BDDMockito.given(ownerService.findEntityById(TEST_OWNER_ID_3)).willReturn(optOwner3);

	}

	//Show Clinic as a Vet
	@WithMockUser(value = "pepito")
	@Test

	void testShowClinicAsVetPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("owners"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}

	//Show Clinic as a Vet
	@WithMockUser(value = "Falsepepito", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicAsVetNegativeNotExistingVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//Show Clinic as a Owner
	@WithMockUser(value = "joselito")
	@Test
	void testShowClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("notUnsubscribe"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("clinic")).andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}

	@WithMockUser(value = "Falsejoselito")
	@Test
	void testShowClinicAsOwnerNegativeNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//Show Clinic from a Owner
	@WithMockUser(value = "manolito")
	@Test
	void testShowListClinicOwnerPositiveClinicNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinics"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testShowListClinicOwnerPositiveClinicNotNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "Falseowner")
	@Test
	void testShowListClinicOwnerNegativeClinicNotNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testShowClinicDetailsAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicDetails"));
	}

	@WithMockUser(value = "Falsejoselito")
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotAuthorizedOtherClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_2_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testUnsubscribeFromClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}

	@WithMockUser(value = "manolito")
	@Test
	void testUnsubscribeFromClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "manolito2")
	@Test
	void testUnsubscribeFromClinicAsOwnerNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "manolito")
	@Test
	void testSubscribeToClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "manolito")
	@Test
	void testSubscribeToClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "Falseowner")
	@Test
	void testSubscribeToClinicAsOwnerNegativeNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "joselito")
	@Test
	void testSubscribeToClinicAsOwnerNegativeOwnerAlreadyInClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

}
