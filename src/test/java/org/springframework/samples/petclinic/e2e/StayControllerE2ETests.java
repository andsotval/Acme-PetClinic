
package org.springframework.samples.petclinic.e2e;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class StayControllerE2ETests {

	private static final int	TEST_STAY_ID	= 1;

	private static final int	TEST_CLINIC1_ID	= 1;

	private static final int	TEST_PET_ID		= 1;

	@Autowired
	private MockMvc				mockMvc;


	//listAllPending (todas las stays devueltas tienen que tener isAcepted a null)
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowStaysPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending")).andExpect(MockMvcResultMatchers.status().isOk())
			/* .andExpect(MockMvcResultMatchers.model().attributeExists("stays")) */.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "vet230", authorities = {
		"veterinarian"
	})
	@Test
	void testShowStaysPendingNegativeUserNotInSystem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//listAllAccepted (todas las stays devueltas tienen que tener isAcepted a true)
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowStaysAccept() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted")).andExpect(MockMvcResultMatchers.status().isOk())
			/* .andExpect(MockMvcResultMatchers.model().attributeExists("stays")) */.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "vet123", authorities = {
		"veterinarian"
	})
	@Test
	void testShowStaysAcceptNegativeUserNotInSystem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));

	}

	//acceptStay (pasarle una stay con isAccepted a null y te la actualice a true)
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", TEST_STAY_ID)).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptStayNotExisting() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", TEST_STAY_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())/* .andExpect(MockMvcResultMatchers.model().attributeExists("nonAuthorized")) */
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//cancelStay (pasarle una stay con isAccepted a null y te la actualice a false)
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID)).andExpect(MockMvcResultMatchers.status().isFound()).andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelStayNotExisting() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	//TODO: Negativo
	//changeDateStay (la stay que entra es la misma que sale)
	@WithMockUser(value = "vet6", authorities = {
		"veterinarian"
	})
	@Test
	void testChangeDateStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", TEST_STAY_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//TODO: No Se valida en ningún caso que el usuario que está
	//cambiando la fecha sea el mismo que del stay
	//	@WithMockUser(value = "owner1", authorities = {
	//		"owner"
	//	})
	//	@Test
	//	void testChangeDateStayNegativeId() throws Exception {
	//		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", )).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("/stays/createOrUpdateStayForm"));
	//	}

	//updateStay (actualizar parametros (startDate, finishDate y description) y comprobar que se ha guardado bien)
	//startDate tiene que estar en futuro
	//diferencia entre startdate y finishDate minimo de un dia, maximo siete
	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testStaySuccesfull() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description").param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testSuccesfullStayNoLongerThan7Days() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "2020/05/22").param("finishDate", "2020/06/23")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "finishDateMinimumOneWeek")).andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testSuccesfullStayDateNotNull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "").param("finishDate", "2020/05/22")
				.param("pet.id", String.valueOf(TEST_PET_ID)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			/* .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startDateNotNull")) */.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testSuccesfullStayFinishDateBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "2021/11/01").param("finishDate", "2021/10/01")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "finishDateAfterStartDate")).andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testSuccesfullStayLessThan2Days() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue()) + "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue()) + "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", startDate).param("finishDate", finishDate)
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days")).andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	// /save
	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStay() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "2020/05/22").param("finishDate", "2020/05/29")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())/* .andExpect(MockMvcResultMatchers.model().attribute("message", "Stay succesfully updated")) */.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listByOwner"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayNoLongerThan7Days() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "2020/05/22").param("finishDate", "2020/06/23")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "finishDateMinimumOneWeek"))
			/* .andExpect(MockMvcResultMatchers.model().attribute("message", "Stay succesfully updated")) */.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayDateNotNull() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "").param("finishDate", "")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "NotNull")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayFinishBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", "2021/11/01").param("finishDate", "2021/10/01")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "finishDateAfterStartDate")).andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayLessThan2Days() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue()) + "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue()) + "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description").param("startDate", startDate).param("finishDate", finishDate)
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days")).andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	// ListByOwner
	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void ListAllPendingByOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner")).andExpect(MockMvcResultMatchers.status().isOk())
			/*
			 * .andExpect(MockMvcResultMatchers.model().attributeExists("staysPending")) * .andExpect(MockMvcResultMatchers.model().attributeExists("staysAccepted"))
			 */.andExpect(MockMvcResultMatchers.view().name("stays/listByOwner"));
	}

	@WithMockUser(value = "owner123", authorities = {
		"owner"
	})
	@Test
	void ListAllPendingByOwnerUserNotInSystem() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}
}
