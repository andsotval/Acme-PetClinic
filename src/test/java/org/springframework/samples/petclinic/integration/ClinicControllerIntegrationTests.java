
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Clinic;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.web.ClinicController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClinicControllerIntegrationTests {

	private static final int	TEST_CLINIC_ID_1	= 1;

	@Autowired
	private ClinicController	clinicController;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private ClinicService		clinicService;


	@SuppressWarnings("unchecked")
	@WithMockUser(username = "vet1", authorities = {
		"vet"
	})
	@Test
	public void testShowClinicAsVetPositive() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.getDetail(model);

		assertEquals(view, "/clinics/clinicDetails");

		assertNotNull(model.get("clinic"));

		List<Owner> list = ownerService.findOwnersByClinicId(TEST_CLINIC_ID_1);
		assertNotNull(model.get("owners"));
		assertEquals(((Collection<Owner>) model.get("owners")).size(), list.size());
		((Collection<Owner>) model.get("owners")).forEach(owner -> {
			list.contains(owner);
		});
	}

	@WithMockUser(value = "vet99", authorities = {
		"veterinarian"
	})
	@Test
	public void testShowClinicAsVetNegativeNotExistingVet() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.getDetail(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicAsOwnerPositive() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.getDetail(model);

		assertEquals(view, "/clinics/clinicDetails");

		assertNotNull(model.get("clinic"));

		Owner owner = ownerService.findPersonByUsername("owner1");
		assertNotNull(model.get("owner"));
		assertEquals(((Owner) model.get("owner")).getAddress(), owner.getAddress());
		assertEquals(((Owner) model.get("owner")).getCity(), owner.getCity());
		assertEquals(((Owner) model.get("owner")).getClinic().getId(), owner.getClinic().getId());
		assertEquals(((Owner) model.get("owner")).getFirstName(), owner.getFirstName());
		assertEquals(((Owner) model.get("owner")).getId(), owner.getId());
		assertEquals(((Owner) model.get("owner")).getLastName(), owner.getLastName());
		assertEquals(((Owner) model.get("owner")).getMail(), owner.getMail());
		assertEquals(((Owner) model.get("owner")).getTelephone(), owner.getTelephone());
		assertEquals(((Owner) model.get("owner")).getUser().getId(), owner.getUser().getId());
	}

	@WithMockUser(value = "owner99", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicAsOwnerNegativeNotExistingOwner() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.getDetail(model);

		assertEquals(view, "redirect:/oups");
	}

	@SuppressWarnings("unchecked")
	@WithMockUser(value = "owner10", authorities = {
		"owner"
	})
	@Test
	public void testShowListClinicOwnerPositiveClinicNull() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.initClinicView(model);

		assertEquals(view, "/clinics/owner/clinicsList");

		Collection<Clinic> list = (Collection<Clinic>) clinicService.findAllEntities();
		assertNotNull(model.get("clinics"));
		assertEquals(((Collection<Clinic>) model.get("clinics")).size(), list.size());
		((Collection<Clinic>) model.get("clinics")).forEach(clinic -> {
			list.contains(clinic);
		});
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowListClinicOwnerPositiveClinicNotNull() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.initClinicView(model);

		assertEquals(view, "redirect:/clinics/getDetail");
	}

	@WithMockUser(value = "owner99", authorities = {
		"owner"
	})
	@Test
	public void testShowListClinicOwnerNegativeClinicNotNull() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.initClinicView(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicDetailsAsOwnerPositive() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.showClinic(TEST_CLINIC_ID_1, model);

		assertEquals(view, "/clinics/owner/clinicDetails");

		Clinic clinic = clinicService.findEntityById(TEST_CLINIC_ID_1).get();
		assertNotNull(model.get("clinic"));
		assertEquals(((Clinic) model.get("clinic")).getAddress(), clinic.getAddress());
		assertEquals(((Clinic) model.get("clinic")).getCity(), clinic.getCity());
		assertEquals(((Clinic) model.get("clinic")).getId(), clinic.getId());
		assertEquals(((Clinic) model.get("clinic")).getManager().getId(), clinic.getManager().getId());
		assertEquals(((Clinic) model.get("clinic")).getName(), clinic.getName());
		assertEquals(((Clinic) model.get("clinic")).getTelephone(), clinic.getTelephone());

		Owner owner = ownerService.findPersonByUsername("owner1");
		assertNotNull(model.get("owner"));
		assertEquals(((Owner) model.get("owner")).getAddress(), owner.getAddress());
		assertEquals(((Owner) model.get("owner")).getCity(), owner.getCity());
		assertEquals(((Owner) model.get("owner")).getClinic().getId(), owner.getClinic().getId());
		assertEquals(((Owner) model.get("owner")).getFirstName(), owner.getFirstName());
		assertEquals(((Owner) model.get("owner")).getId(), owner.getId());
		assertEquals(((Owner) model.get("owner")).getLastName(), owner.getLastName());
		assertEquals(((Owner) model.get("owner")).getMail(), owner.getMail());
		assertEquals(((Owner) model.get("owner")).getTelephone(), owner.getTelephone());
		assertEquals(((Owner) model.get("owner")).getUser().getId(), owner.getUser().getId());
	}

	@WithMockUser(value = "owner99", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicDetailsAsOwnerNegativeNotExistingOwner() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.showClinic(TEST_CLINIC_ID_1, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicDetailsAsOwnerNegativeNotExistingClinic() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.showClinic(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testShowClinicDetailsAsOwnerNegativeNotAuthorizedOtherClinic() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.showClinic(2, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner8", authorities = {
		"owner"
	})
	@Test
	public void testUnsubscribeFromClinicAsOwnerPositive() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.unsubscribeOwnerFromClinic(model);

		assertEquals(view, "redirect:/clinics/getDetail");
	}

	@WithMockUser(value = "owner9", authorities = {
		"owner"
	})
	@Test
	public void testUnsubscribeFromClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.unsubscribeOwnerFromClinic(model);

		assertEquals(view, "redirect:/clinics/getDetail");
	}

	@WithMockUser(value = "owner99", authorities = {
		"owner"
	})
	@Test
	public void testUnsubscribeFromClinicAsOwnerNegative() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.unsubscribeOwnerFromClinic(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner9", authorities = {
		"owner"
	})
	@Test
	public void testSubscribeToClinicAsOwnerPositive() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.subscribeToClinic(TEST_CLINIC_ID_1, model);

		assertEquals(view, "redirect:/clinics/getDetail");
	}

	@WithMockUser(value = "owner10", authorities = {
		"owner"
	})
	@Test
	public void testSubscribeToClinicAsOwnerNegativeNotExistingClinic() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.subscribeToClinic(99, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner99", authorities = {
		"owner"
	})
	@Test
	public void testSubscribeToClinicAsOwnerNegative() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.subscribeToClinic(TEST_CLINIC_ID_1, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(value = "owner1", authorities = {
		"owner"
	})
	@Test
	public void testSubscribeToClinicAsOwnerNegativeOwnerAlreadyInClinic() throws Exception {
		ModelMap model = new ModelMap();
		String view = clinicController.subscribeToClinic(TEST_CLINIC_ID_1, model);

		assertEquals(view, "redirect:/oups");
	}

}
