
package org.springframework.samples.petclinic.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ClinicControllerE2ETests {
	
	private static final int	TEST_CLINIC_ID				= 1;
	private static final int	TEST_CLINIC_2_ID			= 2;
	private static final int	TEST_NOT_EXISTING_CLINIC_ID	= 9;

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicAsVetPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.view().name("/clinics/clinicDetails"));
	}
	
	@WithMockUser(value = "vet99", authorities = {
		"veterinarian"
	})
	@Test
	void testShowClinicAsVetNegativeNotExistingVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "provider1", authorities = {
			"provider"
	})
	@Test
	void testShowClinicAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testShowClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}
	
	@WithMockUser(value = "owner99",authorities= {"owner"})
	@Test
	void testShowClinicAsOwnerNegativeNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/getDetail")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "owner10",authorities= {"owner"})
	@Test
	void testShowListClinicOwnerPositiveClinicNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinics"))
		.andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}
	
	@WithMockUser(value = "owner9", authorities = {
			"owner"
	})
	@Test
	void testShowListClinicOwnerPositiveClinicNotNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicsList"));
	}
	
	@WithMockUser(value = "owner99", authorities = {
			"owner"
	})
	@Test
	void testShowListClinicOwnerNegativeClinicNotNull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "provider1", authorities = {
			"provider"
		})
	@Test
	void testShowListAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owner")).andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@WithMockUser(value = "owner9", authorities = {
			"owner"
	})
	@Test
	void testShowClinicDetailsAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("clinic"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner")).andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicDetails"));
	}

	@WithMockUser(value = "owner99", authorities = {
			"owner"
	})
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotExistingOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner9", authorities = {
			"owner"
	})
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner9", authorities = {
			"owner"
	})
	@Test
	void testShowClinicDetailsAsOwnerNegativeNotAuthorizedOtherClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_2_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/clinics/owner/clinicDetails"));
	}
	
	@WithMockUser(value = "provider1", authorities = {
			"provider"
	})
	@Test
	void testShowClinicDetailsAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/{clinicId}", TEST_CLINIC_2_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@WithMockUser(value = "owner8", authorities = {
			"owner"
		})
	@Test
	void testUnsubscribeFromClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "owner9", authorities = {
			"owner"
		})
	@Test
	void testUnsubscribeFromClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner99", authorities = {
			"owner"
		})
	@Test
	void testUnsubscribeFromClinicAsOwnerNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/unsubscribeFromClinic")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "owner9", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicAsOwnerPositive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/clinics/getDetail"));
	}

	@WithMockUser(value = "owner10", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner99", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicAsOwnerNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "provider1", authorities = {
			"provider"
		})
	@Test
	void testUnsubscribeFromClinicAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@WithMockUser(value = "owner1", authorities = {
			"owner"
		})
	@Test
	void testSubscribeToClinicAsOwnerNegativeOwnerAlreadyInClinic() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
	
	@WithMockUser(value = "provider1", authorities = {
			"provider"
		})
	@Test
	void testSubscribeToClinicAsRoleNotAuthorizated() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clinics/owner/subscribeToClinic/{clinicId}", TEST_NOT_EXISTING_CLINIC_ID)).andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
}
