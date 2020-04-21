
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.ManagerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ClinicController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class ClinicControllerTest {

	private static final int	TEST_OWNER_ID_1		= 1;

	private static final int	TEST_OWNER_ID_2		= 2;

	private static final int	TEST_MANAGER_ID		= 1;

	private static final int	TEST_CLINIC_ID_1	= 1;

	private static final int	TEST_CLINIC_ID_2	= 2;

	private static final int	TEST_CLINIC_ID_3	= 3;

	private static Clinic		clinic1;
	private static Clinic		clinic2;
	private static Clinic		clinic3;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ClinicService		clinicService;

	@MockBean
	private OwnerService		ownerService;

	@MockBean
	private ManagerService		managerService;


	@BeforeEach
	void setup() {

		User userOwner = new User();
		userOwner.setEnabled(true);
		userOwner.setUsername("juan");
		userOwner.setPassword("juan");

		Authorities authorityOwner = new Authorities();
		authorityOwner.setAuthority("owner");
		authorityOwner.setUsername("juan");
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID_1);
		owner.setUser(userOwner);
		owner.setFirstName("Juan");
		owner.setLastName("Leary");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		BDDMockito.given(ownerService.findPersonByUsername("juan")).willReturn(owner);

		User userManager1 = new User();
		userManager1.setEnabled(true);
		userManager1.setUsername("pepito");
		userManager1.setPassword("pepito");

		Authorities authorityManager1 = new Authorities();
		authorityManager1.setAuthority("manager");
		authorityManager1.setUsername("pepito");
		Manager manager1 = new Manager();
		manager1.setUser(userManager1);
		manager1.setId(TEST_MANAGER_ID);
		manager1.setFirstName("Pepe");
		manager1.setLastName("Leary");
		manager1.setAddress("110 W. Liberty St.");
		manager1.setCity("Madison");
		manager1.setTelephone("6085551023");

		Optional<Manager> optionalManager = Optional.of(manager1);
		BDDMockito.given(managerService.findEntityById(TEST_MANAGER_ID)).willReturn(optionalManager);

		User userManager2 = new User();
		userManager2.setEnabled(true);
		userManager2.setUsername("miguel");
		userManager2.setPassword("miguel");

		Authorities authorityManager2 = new Authorities();
		authorityManager2.setAuthority("manager");
		authorityManager2.setUsername("miguel");
		Manager manager2 = new Manager();
		manager2.setUser(userManager2);
		manager2.setId(TEST_MANAGER_ID);
		manager2.setFirstName("miguel");
		manager2.setLastName("Leary");
		manager2.setAddress("110 W. Liberty St.");
		manager2.setCity("Madison");
		manager2.setTelephone("6085551023");

		Optional<Manager> optionalManager2 = Optional.of(manager2);
		BDDMockito.given(managerService.findEntityById(TEST_MANAGER_ID)).willReturn(optionalManager2);

		User userManager3 = new User();
		userManager3.setEnabled(true);
		userManager3.setUsername("mario");
		userManager3.setPassword("mario");

		Authorities authorityManager3 = new Authorities();
		authorityManager3.setAuthority("manager");
		authorityManager3.setUsername("mario");
		Manager manager3 = new Manager();
		manager3.setUser(userManager3);
		manager3.setId(TEST_MANAGER_ID);
		manager3.setFirstName("mario");
		manager3.setLastName("Leary");
		manager3.setAddress("110 W. Liberty St.");
		manager3.setCity("Madison");
		manager3.setTelephone("6085551023");

		Optional<Manager> optionalManager3 = Optional.of(manager3);
		BDDMockito.given(managerService.findEntityById(TEST_MANAGER_ID)).willReturn(optionalManager3);

		Clinic clinic1 = new Clinic();
		clinic1.setId(TEST_CLINIC_ID_1);
		clinic1.setName("Canitas");
		clinic1.setAddress("C/La mina, 8");
		clinic1.setCity("Dos Hermanas");
		clinic1.setTelephone("634987123");
		clinic1.setManager(manager1);

		ClinicControllerTest.clinic1 = clinic1;

		Optional<Clinic> optionalClinic1 = Optional.of(clinic1);
		BDDMockito.given(clinicService.findEntityById(TEST_CLINIC_ID_1)).willReturn(optionalClinic1);
		BDDMockito.given(clinicService.findClinicByManagerId(manager1.getId())).willReturn(clinic1);

		Clinic clinic2 = new Clinic();
		clinic2.setId(TEST_CLINIC_ID_1);
		clinic2.setName("Canitas");
		clinic2.setAddress("C/La mina, 8");
		clinic2.setCity("Dos Hermanas");
		clinic2.setTelephone("634987123");
		clinic2.setManager(manager1);

		ClinicControllerTest.clinic2 = clinic2;

		Optional<Clinic> optionalClinic2 = Optional.of(clinic2);
		BDDMockito.given(clinicService.findEntityById(TEST_CLINIC_ID_1)).willReturn(optionalClinic2);
		BDDMockito.given(clinicService.findClinicByManagerId(manager1.getId())).willReturn(clinic2);

		Clinic clinic3 = new Clinic();
		clinic3.setId(TEST_CLINIC_ID_1);
		clinic3.setName("Canitas");
		clinic3.setAddress("C/La mina, 8");
		clinic3.setCity("Dos Hermanas");
		clinic3.setTelephone("634987123");
		clinic3.setManager(manager1);

		ClinicControllerTest.clinic3 = clinic3;

		Optional<Clinic> optionalClinic3 = Optional.of(clinic3);
		BDDMockito.given(clinicService.findEntityById(TEST_CLINIC_ID_1)).willReturn(optionalClinic3);
		BDDMockito.given(clinicService.findClinicByManagerId(manager1.getId())).willReturn(clinic3);
	}

	//	@WithMockUser(value = "juan")
	//	@Test
	//	void testInitClinicView() throws Exception {
	//		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner"))
	//			.andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/owner/listAvailable"));
	//
	//		mockMvc.perform(MockMvcRequestBuilders.get("clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeExists("clinics"))
	//			.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/listAvailable"));
	//	}

}
