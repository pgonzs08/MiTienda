package es.ugr.dss.MiTienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
	@GetMapping("/login")
	public String login() {
		return "login"; // templates/login.html
	}
	
	@PostMapping("/login")
	public String postLogin() {
		return "redirect:/";
	}

}
