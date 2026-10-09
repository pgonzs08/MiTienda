package es.ugr.dss.MiTienda.controller;

import es.ugr.dss.MiTienda.service.ExportDatabaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExportController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad durante la prueba si es necesario
class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Inyección del mock del servicio de exportación
    private ExportDatabaseService exportService;

    @Test
    @DisplayName("GET /admin/export debe retornar un archivo SQL descargable con estado HTTP 200")
    @WithMockUser
    void export_DeberiaDevolverArchivoSqlConCabecerasCorrectas() throws Exception {
        // Arrange
        String mockSqlContent = "-- Script de exportación\nINSERT INTO product (id, name, price) VALUES (1, 'Teclado', 29.99);\n";
        byte[] mockSqlBytes = mockSqlContent.getBytes(StandardCharsets.UTF_8);

        given(exportService.exportDatabaseToSql()).willReturn(mockSqlBytes);

        // Act & Assert
        mockMvc.perform(get("/admin/export"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"products.sql\""))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(mockSqlBytes));

        verify(exportService).exportDatabaseToSql();
    }
}