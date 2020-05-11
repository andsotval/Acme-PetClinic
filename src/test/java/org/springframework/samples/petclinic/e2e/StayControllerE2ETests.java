
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


	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testListAllPending() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListAllPendingAsOwnerNegativeNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllPending"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testListAllAccepted() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testListAllAcceptedAsOwnerNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listAllAccepted"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testAcceptStayNotAuthorizedNegative() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/accept/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelStayAsVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listAllAccepted"));
	}

	@WithMockUser(value = "vet2", authorities = {
		"veterinarian"
	})
	@Test
	void testCancelStayAsVetNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testCancelStayAsOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listByOwner"));
	}

	@WithMockUser(value = "owner3", authorities = {
		"owner"
	})
	@Test
	void testCancelStayAsOwnerNotAuthorized() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/cancel/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet6", authorities = {
		"veterinarian"
	})
	@Test
	void testShowChangeDateStay() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/changeDate/{stayId}", TEST_STAY_ID))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateStaySuccesful() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/list"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testNegativeUpdateCheckMoreThanOneWeek() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "2020/05/22").param("finishDate", "2020/06/23")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateMinimumOneWeek"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet5", authorities = {
		"veterinarian"
	})
	@Test
	void testNegativeUpdateStayUnauthorized() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Description")
				.param("startDate", "2020/06/09").param("finishDate", "2020/06/10"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/oups"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testUpdateStayNegativeNullDate() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "").param("finishDate", "2020/05/22").param("pet.id", String.valueOf(TEST_PET_ID))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testNegativeUpdateCheckFinishBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save/{stayId}", TEST_STAY_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()).param("id", "").param("description", "Description")
				.param("startDate", "2021/11/01").param("finishDate", "2021/10/01")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateAfterStartDate"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNegativeUpdateStay2DaysFromToday() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue())
			+ "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue())
			+ "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", startDate)
				.param("finishDate", finishDate).param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("stay"))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	// /save
	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayPositive() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2020/05/22")
				.param("finishDate", "2020/05/29").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/stays/listByOwner"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayNegativeCheckMoreThanOneWeek() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2020/05/22")
				.param("finishDate", "2020/06/23").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateMinimumOneWeek"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayNegativeNullDate() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "").param("finishDate", "")
				.param("pet.id", String.valueOf(TEST_PET_ID + 1)).param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate", "NotNull"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayNegativeCheckFinishBeforeStart() throws Exception {
		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", "2021/11/01")
				.param("finishDate", "2021/10/01").param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "finishDate",
				"finishDateAfterStartDate"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void testNewStayNegativeStay2DaysFromToday() throws Exception {
		LocalDate lc1 = LocalDate.now().plusDays(1);
		LocalDate lc2 = LocalDate.now().plusDays(5L);

		String startDate = Integer.valueOf(lc1.getYear()).toString() + "/" + String.format("%02d", lc1.getMonthValue())
			+ "/" + String.format("%02d", lc1.getDayOfMonth());
		String finishDate = Integer.valueOf(lc2.getYear()).toString() + "/" + String.format("%02d", lc2.getMonthValue())
			+ "/" + String.format("%02d", lc2.getDayOfMonth());

		mockMvc
			.perform(MockMvcRequestBuilders.post("/stays/save").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("id", "").param("description", "Description").param("startDate", startDate)
				.param("finishDate", finishDate).param("pet.id", String.valueOf(TEST_PET_ID + 1))
				.param("clinic.id", String.valueOf(TEST_CLINIC1_ID)))
			.andExpect(
				MockMvcResultMatchers.model().attributeHasFieldErrorCode("stay", "startDate", "startFuturePlus2Days"))
			.andExpect(MockMvcResultMatchers.view().name("stays/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	void ListAllPendingByOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("stays/listByOwner"));
	}

	@WithMockUser(value = "provider1", authorities = {
		"provider"
	})
	@Test
	void testNegativeListAllPendingByOtherUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/stays/listByOwner"))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
