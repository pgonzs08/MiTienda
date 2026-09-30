package es.ugr.dss.MiTienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.CartService;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {;
		this.cartService = cartService;
	}

	@GetMapping
	public String cart(Model model) { 
		Map<Product, Integer> cartItems = this.cartService.getProductsInCart();
		model.addAttribute("cartItems", cartItems);
		return "cart"; 
	}

	@PostMapping("/add/{id}")
	public String addToCart(@PathVariable("id") Long id) {
		this.cartService.addProduct(id);
		return "redirect:/cart";
	}

	@PostMapping("/remove/{id}")
	public String removeFromCart(@PathVariable("id") Long id) {
		this.cartService.removeProduct(id);
		return "redirect:/cart";
	}

}