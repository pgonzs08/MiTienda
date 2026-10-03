package es.ugr.dss.MiTienda.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Debe instanciar y asignar/obtener correctamente todas las propiedades del Usuario")
    void testUserGettersAndSetters() {
        // Arrange
        User user = new User();
        String expectedUsername = "juan";
        String expectedPassword = "hashedPassword123";
        String expectedRole = "USER";

        // Act
        user.setUsername(expectedUsername);
        user.setPassword(expectedPassword);
        user.setRole(expectedRole);

        // Assert
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
        assertEquals(expectedRole, user.getRole());
    }
}