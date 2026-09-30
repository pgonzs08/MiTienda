package es.ugr.dss.MiTienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ugr.dss.MiTienda.service.ProductService;


@Controller
public class ViewController {
	
	private final ProductService productService;
	
	public ViewController(ProductService productService) {
        this.productService = productService;
    }
	
	@GetMapping({"/", "/index"})
	public String index(Model model) {
		return "index";     // templates/index.html
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return "admin";     // templates/admin.html
	}

}
