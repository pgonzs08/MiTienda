package es.ugr.dss.MiTienda.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.ugr.dss.MiTienda.MiTiendaApplication;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MiTiendaApplication.class)
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Pruebas de Rutas Públicas (permitAll)")
    class PublicRoutesTests {

        @Test
        @DisplayName("GET / debe ser accesible sin autenticación")
        void publicRoot_DeberiaPermitirAccesoAnonimo() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /cart debe ser accesible sin autenticación")
        void publicCart_DeberiaPermitirAccesoAnonimo() throws Exception {
            mockMvc.perform(get("/cart"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /catalog debe ser accesible sin autenticación")
        void publicProductsGet_DeberiaPermitirAccesoAnonimo() throws Exception {
            mockMvc.perform(get("/catalog"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /login debe mostrar la página de inicio de sesión")
        void publicLoginPage_DeberiaPermitirAccesoAnonimo() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Pruebas de Restricciones por Rol (/admin/**)")
    class AdminRoutesTests {

        @Test
        @DisplayName("GET /admin sin autenticación debe redirigir a /login (401/302)")
        void adminRoute_SinAutenticacion_DeberiaRedirigirALogin() throws Exception {
            mockMvc.perform(get("/admin"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @DisplayName("GET /admin con usuario ROL 'USER' debe responder 403 Forbidden")
        @WithMockUser(username = "user", roles = "USER")
        void adminRoute_ConRolUser_DeberiaDenegarAcceso() throws Exception {
            mockMvc.perform(get("/admin"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("GET /admin con usuario ROL 'ADMIN' debe permitir el acceso")
        @WithMockUser(username = "admin", roles = "ADMIN")
        void adminRoute_ConRolAdmin_DeberiaPermitirAcceso() throws Exception {
            mockMvc.perform(get("/admin"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Pruebas de Autenticación y FormLogin (UserDetailsService)")
    class FormLoginTests {

        @Test
        @DisplayName("Login correcto con credenciales de 'admin' debe autenticar y redirigir a /index")
        void login_ConCredencialesValidasAdmin_DeberiaAutenticarYRedirigir() throws Exception {
            mockMvc.perform(formLogin("/login").user("admin").password("admin123"))
                    .andExpect(authenticated().withUsername("admin").withRoles("ADMIN"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/index"));
        }

        @Test
        @DisplayName("Login correcto con credenciales de 'user' debe autenticar y redirigir a /index")
        void login_ConCredencialesValidasUser_DeberiaAutenticarYRedirigir() throws Exception {
            mockMvc.perform(formLogin("/login").user("user").password("user123"))
                    .andExpect(authenticated().withUsername("user").withRoles("USER"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/index"));
        }

        @Test
        @DisplayName("Login con contraseña incorrecta debe rechazar y no autenticar")
        void login_ConPasswordIncorrecta_DeberiaFallar() throws Exception {
            mockMvc.perform(formLogin("/login").user("admin").password("wrongpassword"))
                    .andExpect(unauthenticated())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error"));
        }

        @Test
        @DisplayName("Logout debe cerrar la sesión y redirigir a /login?logout")
        @WithMockUser
        void logout_DeberiaCerrarSesionYRedirigir() throws Exception {
            mockMvc.perform(logout("/logout"))
                    .andExpect(unauthenticated())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?logout"));
        }
    }

    @Nested
    @DisplayName("Pruebas de Protección CSRF")
    class CsrfProtectionTests {

        @Test
        @DisplayName("POST sin token CSRF en una ruta protegida debe fallar con 403 Forbidden")
        @WithMockUser
        void getSinCsrf_DeberiaSerRechazado() throws Exception {
            mockMvc.perform(get("/admin/catalog/add"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("POST con token CSRF válido debe ser procesado")
        @WithMockUser(roles = "ADMIN")
        void getConCsrf_DeberiaSerAceptado() throws Exception {
            mockMvc.perform(get("/admin/catalog/add").with(csrf()))
                    .andExpect(status().isOk());
        }
    }
}