
package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = PetController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PetControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	private static final int	TEST_PET_ID		= 1;

	@Autowired
	private PetController		petController;

	@MockBean
	private PetService			petService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
		BDDMockito.given(this.ownerService.findOwnerById(PetControllerTests.TEST_OWNER_ID)).willReturn(new Owner());
		BDDMockito.given(this.petService.findPetById(PetControllerTests.TEST_PET_ID).get()).willReturn(new Pet());
	}

	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testInitCreationForm() throws Exception {
	//		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID)).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//        @Test
	//	void testProcessCreationFormSuccess() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("type", "hamster")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("redirect:/owners/{ownerId}"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//    @Test
	//	void testProcessCreationFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(model().attributeHasNoErrors("owner"))
	//				.andExpect(model().attributeHasErrors("pet"))
	//				.andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}
	//
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testInitUpdateForm() throws Exception {
	//		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
	//				.andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}
	//    
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormSuccess() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("type", "hamster")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(status().is3xxRedirection())
	//				.andExpect(view().name("redirect:/owners/{ownerId}"));
	//	}
	//    
	//    @WithMockUser(value = "spring")
	//	@Test
	//	void testProcessUpdateFormHasErrors() throws Exception {
	//		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
	//							.with(csrf())
	//							.param("name", "Betty")
	//							.param("birthDate", "2015/02/12"))
	//				.andExpect(model().attributeHasNoErrors("owner"))
	//				.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
	//				.andExpect(view().name("pets/createOrUpdatePetForm"));
	//	}

}
