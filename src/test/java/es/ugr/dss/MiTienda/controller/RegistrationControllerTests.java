package es.ugr.dss.MiTienda.controller;

import es.ugr.dss.MiTienda.model.User;
import es.ugr.dss.MiTienda.repository.UserRepo;
import es.ugr.dss.MiTienda.service.CustomUserDetailsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad para centrarse en el controlador
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepo userRepo;
    
    @MockitoBean
    private CustomUserDetailsService userService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("POST /register debe guardar el usuario con contraseña cifrada y redirigir a /login?registered si el usuario no existe")
    @WithMockUser
    void registerUser_DeberiaRegistrarUsuarioYRedirigir_CuandoUsuarioNoExiste() throws Exception {
        // Arrange
        String username = "nuevoUsuario";
        String rawPassword = "password123";
        String encodedPassword = "encoded_password123";

        given(userRepo.existsByUsername(username)).willReturn(false);
        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", rawPassword)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        // Verificaciones
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepo).save(argThat(user -> 
                user.getUsername().equals(username) &&
                user.getPassword().equals(encodedPassword) &&
                user.getRole().equals("USER")
        ));
    }

    @Test
    @DisplayName("POST /register debe retornar la vista 'register' con mensaje de error si el usuario ya existe")
    @WithMockUser
    void registerUser_DeberiaMostrarError_CuandoUsuarioYaExiste() throws Exception {
        // Arrange
        String username = "usuarioExistente";
        String rawPassword = "password123";

        given(userRepo.existsByUsername(username)).willReturn(true);

        // Act & Assert
        mockMvc.perform(post("/register")
                        .param("username", username)
                        .param("password", rawPassword)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "El nombre de usuario ya está registrado."));

        // Verificar que no se cifra ni se intenta guardar nada
        verify(passwordEncoder, never()).encode(any());
        verify(userRepo, never()).save(any(User.class));
    }
}