package es.ugr.dss.MiTienda.service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("getAllProducts debe devolver la lista de productos devuelta por el repositorio")
    void getAllProducts_DeberiaDevolverListaDeProductos() {
        // Arrange
        Product producto1 = new Product();
        Product producto2 = new Product();
        List<Product> productosEsperados = List.of(producto1, producto2);

        given(productRepo.findAll()).willReturn(productosEsperados);

        // Act
        List<Product> resultado = productService.getAllProducts();

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado).isEqualTo(productosEsperados);
        verify(productRepo).findAll();
    }

    @Test
    @DisplayName("getProductById debe devolver el producto correspondiente según su id")
    void getProductById_DeberiaDevolverProductoPorId() {
        // Arrange
        Long productId = 1L;
        Product productoEsperado = new Product();
        given(productRepo.getReferenceById(productId)).willReturn(productoEsperado);

        // Act
        Product resultado = productService.getProductById(productId);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEqualTo(productoEsperado);
        verify(productRepo).getReferenceById(productId);
    }

    @Test
    @DisplayName("saveProduct debe invocar al método save del repositorio")
    void saveProduct_DeberiaLlamarAlMetodoSaveDelRepositorio() {
        // Arrange
        Product productoAGuardar = new Product();

        // Act
        productService.saveProduct(productoAGuardar);

        // Assert
        verify(productRepo).save(productoAGuardar);
    }

    @Test
    @DisplayName("deleteProduct debe invocar al método deleteById del repositorio")
    void deleteProduct_DeberiaLlamarAlMetodoDeleteByIdDelRepositorio() {
        // Arrange
        Long productId = 1L;

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepo).deleteById(productId);
    }
}