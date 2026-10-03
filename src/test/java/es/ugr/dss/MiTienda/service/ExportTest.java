package es.ugr.dss.MiTienda.service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private ProductRepo productRepo;
    
    @InjectMocks
    private ExportDatabaseService exportDatabaseService;

    @Test
    @DisplayName("Debe generar el script SQL en byte[] con los datos de los productos formateados")
    void exportDatabaseToSql_DeberiaGenerarScriptSqlCorrecto() {
        // Arrange
        Product producto1 = new Product();
        producto1.setId(1L);
        producto1.setName("Teclado Mecánico");
        producto1.setPrice(49.99);
        
        Product producto2 = new Product();
        producto2.setId(2L);
        producto2.setName("Ratón Gaming");
        producto2.setPrice(25.50);

        productRepo.save(producto1);
        productRepo.save(producto2);
        
        given(productRepo.findAll()).willReturn(List.of(producto1, producto2));

        byte[] resultBytes = exportDatabaseService.exportDatabaseToSql();
        String resultSql = new String(resultBytes, StandardCharsets.UTF_8);
        
        assertNotNull(resultBytes);
        assertTrue(resultBytes.length > 0);
        
        assertTrue(resultSql.contains("-- Script de exportación de productos"));
        assertTrue(resultSql.contains("-- Generado automáticamente"));
        assertTrue(resultSql.contains("INSERT INTO product (id, name, price) VALUES (1, 'Teclado Mecánico', 49.99);"));
        assertTrue(resultSql.contains("INSERT INTO product (id, name, price) VALUES (2, 'Ratón Gaming', 25.50);"));

        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("Debe escapar correctamente las comillas simples en el nombre del producto")
    void exportDatabaseToSql_DeberiaEscaparComillasSimples() {
        // Arrange
        Product productoConComilla = new Product();
        productoConComilla.setId(3L);
        productoConComilla.setName("L'Oreal Champú");
        productoConComilla.setPrice(12.90);

        given(productRepo.findAll()).willReturn(List.of(productoConComilla));

        // Act
        byte[] resultBytes = exportDatabaseService.exportDatabaseToSql();
        String resultSql = new String(resultBytes, StandardCharsets.UTF_8);
        
        // Assert
        assertTrue(resultSql.contains("INSERT INTO product (id, name, price) VALUES (3, 'L''Oreal Champú', 12.90);"));

        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("Debe generar la cabecera del script sin sentencias INSERT si no hay productos")
    void exportDatabaseToSql_DeberiaGenerarScriptVacio_CuandoNoHayProductos() {
        // Arrange
        given(productRepo.findAll()).willReturn(Collections.emptyList());

        // Act
        byte[] resultBytes = exportDatabaseService.exportDatabaseToSql();
        String resultSql = new String(resultBytes, StandardCharsets.UTF_8);

        // Assert
        assertNotNull(resultBytes);
        assertTrue(resultSql.contains("-- Script de exportación de productos"));
        assertFalse(resultSql.contains("INSERT INTO product"));

        verify(productRepo).findAll();
    }
}