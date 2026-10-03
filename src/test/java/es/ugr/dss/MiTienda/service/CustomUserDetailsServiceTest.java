package es.ugr.dss.MiTienda.service;

import es.ugr.dss.MiTienda.model.User;
import es.ugr.dss.MiTienda.repository.UserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("loadUserByUsername debe retornar un UserDetails válido cuando el usuario existe")
    void loadUserByUsername_DeberiaRetornarUserDetails_CuandoUsuarioExiste() {
        // Arrange
        String username = "usuarioEjemplo";
        User user = new User();
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setRole("USER"); // Si usas ROLE_USER o USER, asegura que el mapper lo procese

        given(userRepo.findByUsername(username)).willReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        
        // Verificar que contiene la autoridad ROLE_USER
        boolean hasUserRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_USER"));
        assertTrue(hasUserRole, "El usuario debería tener la autoridad ROLE_USER");

        verify(userRepo).findByUsername(username);
    }

    @Test
    @DisplayName("loadUserByUsername debe lanzar UsernameNotFoundException cuando el usuario no existe")
    void loadUserByUsername_DeberiaLanzarExcepcion_CuandoUsuarioNoExiste() {
        // Arrange
        String username = "usuarioInexistente";
        given(userRepo.findByUsername(username)).willReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );

        assertTrue(exception.getMessage().contains(username));
        verify(userRepo).findByUsername(username);
    }
}