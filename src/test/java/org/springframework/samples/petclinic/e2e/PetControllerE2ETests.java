
package org.springframework.samples.petclinic.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class PetControllerE2ETests {

	private static final int	TEST_OWNER_ID		= 1;

	private static final int	TEST_PET_ID			= 1;
	private static final int	TEST_PET_ID_2		= 2;
	private static final int	TEST_PET_ID_WRONG	= 555;

	@Autowired
	private MockMvc				mockMvc;


	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListMyPetsPositive() throws Exception {
		mockMvc.perform(get("/pets/listMyPets")).andExpect(status().isOk()).andExpect(model().attributeExists("types"))
			.andExpect(model().attributeExists("pets")).andExpect(view().name("pets/list"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestListMyPetsAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/listMyPets")).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewPetPositive() throws Exception {
		mockMvc.perform(get("/pets/new")).andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
			.andExpect(model().attributeExists("types")).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testNewPetAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/new")).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testSavePetPositive() throws Exception {
		mockMvc
			.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11")
				.param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID)))
			.andExpect(status().isOk()).andExpect(view().name("pets/list"));

	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestSavePetNegativeBadBirthDate() throws Exception {
		mockMvc
			.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2021/04/11")
				.param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID)))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "birthDateFuture"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestSavePetNegativeBadName() throws Exception {
		mockMvc
			.perform(post("/pets/save").with(csrf()).param("name", "wn").param("birthDate", "2019/04/11")
				.param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID)))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("pet", "name"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestSavePetNegativeBadType() throws Exception {
		mockMvc
			.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11")
				.param("type", "wrongType").param("owner.id", String.valueOf(TEST_OWNER_ID)))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));

	}

	@WithMockUser(value = "owner2", authorities = {
		"owner"
	})
	@Test
	void TestSavePetNegativeBadOwner() throws Exception {
		mockMvc
			.perform(post("/pets/save").with(csrf()).param("name", "rex").param("birthDate", "2019/04/11")
				.param("type", "dog").param("owner.id", String.valueOf(TEST_OWNER_ID)))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));

	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDeletePetPositive() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID)).andExpect(status().isOk())
			.andExpect(view().name("pets/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestDeletePetNegativeNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID_2)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testDeletePetNotPresent() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestDeletePetAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/delete/{petId}", TEST_PET_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewVisitPositive() throws Exception {
		mockMvc.perform(get("/pets/newVisit/{petId}", TEST_PET_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("clinicId")).andExpect(model().attributeExists("visit"))
			.andExpect(model().attributeExists("visits")).andExpect(view().name("visits/createOrUpdateVisitForm"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestNewVisitAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/newVisit/{petId}", TEST_PET_ID)).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestNewVisitPetNotPresent() throws Exception {
		mockMvc.perform(get("/pets/newVisit/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection())
			.andExpect(model().attributeDoesNotExist("clinicId")).andExpect(model().attributeDoesNotExist("visit"))
			.andExpect(model().attributeDoesNotExist("visits")).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayPositive() throws Exception {
		mockMvc.perform(get("/pets/newStay/{petId}", TEST_PET_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("stay")).andExpect(model().attributeExists("stays"))
			.andExpect(view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestNewStayAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/newStay/{petId}", TEST_PET_ID_WRONG)).andExpect(status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestNewStayPetNotPresent() throws Exception {
		mockMvc.perform(get("/pets/newStay/{petId}", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection())
			.andExpect(model().attributeDoesNotExist("clinicId")).andExpect(model().attributeDoesNotExist("stay"))
			.andExpect(model().attributeDoesNotExist("stays")).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestInitUpdateFormPositive() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("pet")).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestInitUpdateFormPetNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID_2)).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void TestInitUpdateFormPetNotPresent() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID_WRONG)).andExpect(status().is3xxRedirection())
			.andExpect(model().attributeDoesNotExist("pet")).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void TestInitUpdateFormAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(get("/pets/{petId}/edit", TEST_PET_ID_WRONG)).andExpect(status().isForbidden());
	}
}
