package es.ugr.dss.MiTienda.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ugr.dss.MiTienda.model.User;
import es.ugr.dss.MiTienda.repository.UserRepo;

@Controller
public class RegistrationController {

	@Autowired
    private UserRepo userRepo;
	@Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {

    	if (userRepo.existsByUsername(username)) {
            model.addAttribute("error", "El nombre de usuario ya está registrado.");
            return "register";
        }

        // 2. Crear y configurar la nueva entidad User
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); 
        newUser.setRole("USER");

        // 3. Persistir en la base de datos mediante JPA
        userRepo.save(newUser);

        // Redirigir al login informando del éxito
        return "redirect:/login?registered";
    }
}