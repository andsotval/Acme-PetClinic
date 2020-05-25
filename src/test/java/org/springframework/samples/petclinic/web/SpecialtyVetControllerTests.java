
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("hsqldb")
@WebMvcTest(value = SpecialtyVetController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration = SecurityConfiguration.class)
public class SpecialtyVetControllerTests {

	private static final int	TEST_SPECIALTY_ID_1		= 1;

	private static final int	TEST_SPECIALTY_ID_2		= 2;

	private static final int	TEST_SPECIALTY_ID_99	= 99;

	private static final String	VIEW_REDIRECT_OUPS		= "redirect:/oups";

	private static final String	VIEW_SPECIALTY_VET_LIST	= "/specialty/vet/list";

	private static final int	TEST_VET1_ID			= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private SpecialtyService	specialtyService;

	@MockBean
	private VetService			vetService;


	@BeforeEach
	void setup() {
		Specialty specialty1 = new Specialty();
		specialty1.setId(TEST_SPECIALTY_ID_1);
		specialty1.setName("type1");
		specialty1.setAvailable(true);

		Specialty specialty2 = new Specialty();
		specialty2.setId(TEST_SPECIALTY_ID_2);
		specialty2.setName("type2");
		specialty2.setAvailable(true);

		BDDMockito.given(specialtyService.findAvailable()).willReturn(Lists.newArrayList(specialty1, specialty2));

		Optional<Specialty> optSpecialty = Optional.of(specialty1);
		BDDMockito.given(specialtyService.findEntityById(TEST_SPECIALTY_ID_1)).willReturn(optSpecialty);

		User user = new User();
		user.setEnabled(true);
		user.setUsername("pepito");
		user.setPassword("pepito");

		Authorities authority = new Authorities();
		authority.setAuthority("veterinarian");
		authority.setUsername("pepito");
		Vet james = new Vet();
		james.setUser(user);
		james.setId(TEST_VET1_ID);
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setAddress("110 W. Liberty St.");
		james.setCity("Madison");
		james.setTelephone("6085551023");

		BDDMockito.given(vetService.findPersonByUsername("pepito")).willReturn(james);
	}

	@WithMockUser(value = "pepito")
	@Test
	void testList() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/list"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testAdd() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/add/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "Specialty succesfully added"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testAddValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/add/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testRemove() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/remove/{specialtyId}", TEST_SPECIALTY_ID_1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("specialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("mySpecialties"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "Specialty succesfully removed"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_SPECIALTY_VET_LIST));
	}

	@WithMockUser(value = "pepito")
	@Test
	void testRemoveValueNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/specialty/vet/remove/{specialtyId}", TEST_SPECIALTY_ID_99))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name(VIEW_REDIRECT_OUPS));
	}

}
