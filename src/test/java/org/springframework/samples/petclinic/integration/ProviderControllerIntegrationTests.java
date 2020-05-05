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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class ProviderControllerIntegrationTests {

	private static final int	TEST_PROVIDER_ID_1	= 1;

	@Autowired
	private ProviderController	providerController;

	@Autowired
	private ProviderService		providerService;

	@Autowired
	private ProductService		productService;

	@WithMockUser(username="manager1",authorities= {"manager"})
	@SuppressWarnings({ "unchecked"})
	@Test
	public void TestListAvailable() {
		ModelMap model = new ModelMap();
		String view = providerController.listAvailable(model);

		assertEquals(view, "providers/providersList");

		Collection<Provider> list = providerService.findAvailableProviders();

		assertNotNull(model.get("providers"));
		assertEquals(((Collection<Provider>) model.get("providers")).size(), list.size());
		((Collection<Provider>) model.get("providers")).forEach(provider -> {
			list.contains(provider);
		});
	}
	@WithMockUser(username="provider1",authorities= {"provider"})
	@Test
	public void TestListAvailableNegative() {
		ModelMap model = new ModelMap();
		String view = providerController.listAvailable(model);

		assertEquals(view, "exception");
	}
	@WithMockUser(username="manager1",authorities= {"manager"})
	@Test
	public void testInitAddProviderToManager() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(TEST_PROVIDER_ID_1, model);

		assertEquals(view, "redirect:/providers/listAvailable");

		assertNotNull(model.get("provider"));
		assertEquals(((Provider) model.get("provider")).getManager(), false);
	}

	@WithMockUser(username="provider1",authorities= {"provider"})
	@Test
	public void testInitAddProviderToManagerNegative() {
		ModelMap model = new ModelMap();
		String view = providerController.initAddProviderToManager(TEST_PROVIDER_ID_1, model);

		assertEquals(view, "exception");
	}
	@WithMockUser(username="manager1",authorities= {"manager"})
	@SuppressWarnings("unchecked")
	@Test
	public void testListProductsByProvider() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(TEST_PROVIDER_ID_1,model);

		assertEquals(view, "providers/providerProductsList");

		Collection<Product> list = productService.findProductsAvailableByProviderId(TEST_PROVIDER_ID_1);

		assertNotNull(model.get("products"));
		assertEquals(((Collection<Product>) model.get("products")).size(), list.size());
		((Collection<Product>) model.get("products")).forEach(product -> {
			list.contains(product);
		});

	}
	@WithMockUser(username="provider1",authorities= {"provider"})
	@Test
	public void testListProductsByProviderNegative() {
		ModelMap model = new ModelMap();
		String view = providerController.listProductsByProvider(99, model);

		assertEquals(view, "exception");
	}

}
