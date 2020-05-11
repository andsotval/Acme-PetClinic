
package org.springframework.samples.petclinic.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.web.ProviderController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class ProviderControllerIntegrationTests {

	private static final int	TEST_PROVIDER_ID_1	= 1;

	private static final int	TEST_PROVIDER_ID_2	= 2;

	private static final int	TEST_PROVIDER_ID_3	= 3;

	@Autowired
	private ProviderController	providerController;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private ProductService		productService;


	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@SuppressWarnings({
		"unchecked"
	})
	@Test
	public void TestListAvailable() {
		ModelMap model = new ModelMap();
		String view = providerController.listAvailable(model);

		assertEquals(view, "providers/providersList");

		Collection<Provider> list = providerService.findAvailableProviders();

		assertNotNull(model.get("availableProviders"));
		assertEquals(((Collection<Provider>) model.get("availableProviders")).size(), list.size());
		((Collection<Provider>) model.get("availableProviders")).forEach(provider -> {
			list.contains(provider);
		});
	}

	@WithMockUser(username = "provider1", authorities = {
		"provider"
	})
	@Test
	public void TestListAvailableAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = providerController.listAvailable(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestListAvailableAsManagerNotPresent() {
		ModelMap model = new ModelMap();
		String view = providerController.listAvailable(model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestInitAddProviderToManager() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(TEST_PROVIDER_ID_2, model);

		assertEquals(view, "providers/providersList");

		assertNotNull(model.get("availableProviders"));
		assertNotNull(model.get("addedProviders"));
		assertNotNull(model.get("message"));
	}

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestInitAddProviderToManagerAsManagerNotPresent() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(TEST_PROVIDER_ID_1, model);

		assertEquals(view, "redirect:/oups");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestInitAddProviderToManagerNegativeNotExistingProvider() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(99, model);

		assertEquals(view, "providers/providersList");

		assertNotNull(model.get("availableProviders"));
		assertNotNull(model.get("addedProviders"));
		assertNotNull(model.get("message"));
		assertEquals(model.get("message"), "We are very sorry, but the selected provider does not exist");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestInitAddProviderToManagerNegativeAlreadyAddedProvider() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(TEST_PROVIDER_ID_2, model);

		assertEquals(view, "providers/providersList");

		assertNotNull(model.get("availableProviders"));
		assertNotNull(model.get("addedProviders"));
		assertNotNull(model.get("message"));
		assertEquals(model.get("message"),
			"We are very sorry, it is not possible to add a Provider that is already assigned to another Manager");
	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@SuppressWarnings("unchecked")
	@Test
	public void TestListProductsByProvider() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(TEST_PROVIDER_ID_3, model);

		assertEquals(view, "providers/providerProductsList");

		Collection<Product> list = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID_3);

		assertNotNull(model.get("products"));
		assertEquals(((Collection<Product>) model.get("products")).size(), list.size());
		((Collection<Product>) model.get("products")).forEach(product -> {
			list.contains(product);
		});

	}

	@WithMockUser(username = "manager99", authorities = {
		"manager"
	})
	@Test
	public void TestListProductsByProviderAsManagerNotPresent() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(TEST_PROVIDER_ID_1, model);

		assertEquals(view, "redirect:/oups");

	}

	@WithMockUser(username = "provider1", authorities = {
		"provider"
	})
	@Test
	public void TestListProductsByProviderAsRoleNotAuthorizated() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(TEST_PROVIDER_ID_1, model);

		assertEquals(view, "redirect:/oups");

	}

	@WithMockUser(username = "manager1", authorities = {
		"manager"
	})
	@Test
	public void TestListProductsByProviderNotExistingProvider() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(99, model);

		assertEquals(view, "providers/providersList");

		assertNotNull(model.get("availableProviders"));
		assertNotNull(model.get("addedProviders"));
		assertNotNull(model.get("message"));
		assertEquals(model.get("message"), "We are very sorry, but the selected provider does not exist");
	}

}
