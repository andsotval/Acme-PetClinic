
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

	private static final String	REDIRECT_OUPS						= "redirect:/oups";

	private static final String	REDIRECT_LIST_MY_PRODUCTS			= "redirect:/product/myProductsList";

	private static final String	VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM	= "products/createOrUpdateProductForm";

	private ProductService		productService;

	private ProviderService		providerService;


	@Autowired
	public ProductController(ProductService productService, ProviderService providerService) {
		this.productService = productService;
		this.providerService = providerService;
	}

	@GetMapping(path = "/myProductsList")
	public String list(ModelMap model) {
		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (provider == null)
			return REDIRECT_OUPS;

		Collection<Product> productList = productService.findAllProductsByProvider(provider.getId());
		model.addAttribute("products", productList);

		return "products/listAll";
	}

	@GetMapping(path = "/initNewProduct")
	public String initNewProduct(ModelMap model) {
		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (provider == null)
			return REDIRECT_OUPS;

		Product product = new Product();
		product.setId(null);
		product.setProvider(provider);
		product.setAvailable(true);
		model.addAttribute("product", product);
		return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save")
	public String newProduct(@Valid Product product, BindingResult result, ModelMap model) {
		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		if (provider == null)
			return REDIRECT_OUPS;

		if (result.hasErrors()) {
			model.addAttribute(product);
			return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		}
		productService.saveEntity(product);
		return REDIRECT_LIST_MY_PRODUCTS;
	}

	@GetMapping(path = "/show/{productId}")
	public String initUpdateProduct(@PathVariable("productId") int productId, ModelMap model) {

		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Optional<Product> product = productService.findEntityById(productId);

		if (provider == null)
			return REDIRECT_OUPS;
		if (!product.isPresent())
			return REDIRECT_OUPS;
		if (!provider.getId().equals(product.get().getProvider().getId()))
			return REDIRECT_OUPS;

		model.addAttribute("product", product.get());
		return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(path = "/save/{productId}")
	public String updateProduct(@PathVariable("productId") int productId, @Valid Product productForm, BindingResult result, ModelMap model) {

		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Optional<Product> product = productService.findEntityById(productId);

		if (provider == null)
			return REDIRECT_OUPS;
		if (!product.isPresent())
			return REDIRECT_OUPS;
		if (!provider.getId().equals(product.get().getProvider().getId()))
			return REDIRECT_OUPS;

		if (result.hasErrors()) {
			model.addAttribute(product);
			return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		}
		productService.saveEntity(productForm);
		return REDIRECT_LIST_MY_PRODUCTS;
	}

	//this method changes the state of Available.
	@GetMapping(path = "/desactivateProduct/{productId}")
	public String changeStateAvailableProduct(@PathVariable("productId") int productId, ModelMap model) {
		Provider provider = providerService.findPersonByUsername(SessionUtils.obtainUserInSession().getUsername());
		Product product = productService.findEntityById(productId).get();
		if (provider == null)
			return REDIRECT_OUPS;

		if (!provider.getId().equals(product.getProvider().getId()))
			return REDIRECT_OUPS;

		product.setAvailable(!product.getAvailable());
		productService.saveEntity(product);

		return REDIRECT_LIST_MY_PRODUCTS;
	}

}
