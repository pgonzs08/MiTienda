package es.ugr.dss.MiTienda.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService productService;

	@GetMapping("/products")
	public String getProductsPage(Model model, 
			@RequestParam(name="query", required = false) String name,
			@RequestParam(name="minPrice", required = false) Double min,
			@RequestParam(name="maxPrice", required = false) Double max) {
		
		List<Product> products = productService.getFilteredProducts(name, min, max);
		model.addAttribute("products", products);
		return "products"; // Devuelve products.html desde las plantillas
	}

	@GetMapping("/products/edit/{id}")
	public String editForm(Model model, @PathVariable Long id) {
		model.addAttribute("product", productService.getProductById(id));
		return "product_form";
	}
	
	@PostMapping("/products/add")
	public String addProduct(@RequestParam String name, @RequestParam double price) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		productService.saveProduct(product);
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}


	@PostMapping("/products/update/{id}")
	public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
		Product product = productService.getProductById(id);
		if (product != null) {
			product.setName(name);
			product.setPrice(price);
			productService.saveProduct(product);
		}
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}
	@PostMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return "redirect:/admin";  // Redirigir correctamente a /admin
	}
}