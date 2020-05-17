
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ProviderService;
import org.springframework.samples.petclinic.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

	private static final String	REDIRECT_OUPS	= "redirect:/oups";

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
		model.addAttribute("product", null);
		model.addAttribute("provider", provider);
		return "products/createOrUpdateProductForm";

	}

}
