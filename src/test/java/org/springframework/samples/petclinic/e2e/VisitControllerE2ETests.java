
package org.springframework.samples.petclinic.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class VisitControllerE2ETests {

	private int		TEST_PENDING_VISIT_ID					= 4;

	private int		TEST_PENDING_VISIT_ID_NOT_AUTHORIZED	= 8;

	private int		TEST_VISIT_ID_NOT_FOUND					= 90;

	private int		TEST_ACCEPTED_VISIT_ID					= 1;

	private int		TEST_CLINIC_ID							= 1;

	private int		TEST_PET_ID								= 1;

	@Autowired
	private MockMvc	mockMvc;


	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testListAllPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
			.andExpect(MockMvcResultMatchers.model().attribute("accepted", false))
			.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListAllPendingNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllPending")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testListAllAccepted() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
			.andExpect(model().attribute("accepted", true)).andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListAllAcceptedNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listAllAccepted")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_PENDING_VISIT_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listAllAccepted"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testAcceptVisitAsOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptVisitNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptVisitNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/accept/{visitId}", TEST_VISIT_ID_NOT_FOUND))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelVisitAsVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listAllAccepted"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelVisitAsVetNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "owner3", authorities = {
		"owner"
	})
	@Test
	void testCancelVisitAsOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/visits/listByOwner"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testCancelVisitAsOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/cancel/{visitId}", TEST_PENDING_VISIT_ID_NOT_AUTHORIZED))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testInitUpdateVisit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", TEST_PENDING_VISIT_ID))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("visit"))
			.andExpect(MockMvcResultMatchers.view().name("visits/createOrUpdateVisitForm"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testInitUpdateVisitNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", TEST_VISIT_ID_NOT_FOUND))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testInitUpdateVisitAsVetNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", TEST_ACCEPTED_VISIT_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testInitUpdateVisitAsOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/changeDate/{visitId}", TEST_ACCEPTED_VISIT_ID))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateVisit() throws Exception {
		mockMvc
			.perform(post("/visits/save/{visitId}", TEST_PENDING_VISIT_ID).with(csrf())
				.param("description", "description of the visit").param("dateTime", "2020/08/11 08:30:00"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("messageSuccesful"))
			.andExpect(MockMvcResultMatchers.view().name("visits/list"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateVisitWrongDate() throws Exception {
		mockMvc
			.perform(post("/visits/save/{visitId}", TEST_PENDING_VISIT_ID).with(csrf())
				.param("description", "description of the visit").param("dateTime", "2019/08/11 08:30:00"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("visit"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "dateTime", "dateInFuture"))
			.andExpect(MockMvcResultMatchers.view().name("visits/createOrUpdateVisitForm"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateVisitAsVetNotAuthorized() throws Exception {
		mockMvc
			.perform(post("/visits/save/{visitId}", TEST_PENDING_VISIT_ID).with(csrf())
				.param("description", "description of the visit").param("dateTime", "2020/08/11 08:30:00"))
			.andExpect(status().isOk()).andExpect(view().name("visits/list"));

	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testUpdateVisitAsOwnerNotAuthorized() throws Exception {
		mockMvc
			.perform(post("/visits/save/{visitId}", TEST_PENDING_VISIT_ID).with(csrf())
				.param("description", "description of the visit").param("dateTime", "2020/08/11 08:30:00"))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));

	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateVisitNotFound() throws Exception {
		mockMvc
			.perform(post("/visits/save/{visitId}", TEST_VISIT_ID_NOT_FOUND).with(csrf())
				.param("description", "description of the visit").param("dateTime", "2020/08/11 08:30:00"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testCreateVisit() throws Exception {
		mockMvc
			.perform(post("/visits/save").with(csrf()).param("description", "description of the visit")
				.param("dateTime", "2020/08/11 08:30:00").param("clinic.id", String.valueOf(TEST_CLINIC_ID))
				.param("pet.id", String.valueOf(TEST_PET_ID)))
			.andExpect(status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/visits/listByOwner"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testCreateVisitWrongDate() throws Exception {
		mockMvc
			.perform(post("/visits/save").with(csrf()).param("description", "description of the visit")
				.param("dateTime", "2019/08/11 08:30:00").param("clinic.id", String.valueOf(TEST_CLINIC_ID))
				.param("pet", String.valueOf(TEST_PET_ID)))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("visit"))
			.andExpect(model().attributeHasFieldErrorCode("visit", "dateTime", "dateInFuture"))
			.andExpect(MockMvcResultMatchers.view().name("visits/createOrUpdateVisitForm"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testCreateVisitAsVetNotAuthorized() throws Exception {
		mockMvc
			.perform(post("/visits/save").with(csrf()).param("description", "description of the visit")
				.param("dateTime", "2020/08/11 08:30:00").param("clinic.id", String.valueOf(TEST_CLINIC_ID))
				.param("pet.id", String.valueOf(TEST_PET_ID)))
			.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/oups"));
	}

	@WithMockUser(username = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListAllPendingAndAcceptedByOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listByOwner"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("visitsPending"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("visitsAccepted"))
			.andExpect(MockMvcResultMatchers.view().name("visits/listByOwner"));
	}

	@WithMockUser(username = "vet4", authorities = {
		"veterinarian"
	})
	@Test
	void testListAllPendingAndAcceptedByOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/visits/listByOwner")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/oups"));
	}

}
