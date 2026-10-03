package es.ugr.dss.MiTienda.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.ProductService;

@Controller
public class ProductController {

	private final ProductService productService;

	ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/catalog")
	public String getProductsPage(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "products"; // Devuelve products.html desde las plantillas
	}
	@GetMapping("/admin/catalog/add")
	public String showAddForm(Model model) {
		model.addAttribute("product", new Product());
		return "product_form"; // templates/product_form.html
	}

	@GetMapping("/admin/catalog/edit/{id}")
	public String editForm(Model model, @PathVariable Long id) {
		model.addAttribute("product", productService.getProductById(id));
		return "product_form";
	}
	
	@PostMapping("/admin/catalog/add")
	public String addProduct(@RequestParam String name, @RequestParam double price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		productService.saveProduct(product);
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}


	@PostMapping("/admin/catalog/update/{id}")
	public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
		Product product = productService.getProductById(id);
		if (product != null) {
			product.setName(name);
			product.setPrice(price);
			productService.saveProduct(product);
		}
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}
	@PostMapping("/admin/catalog/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}
}