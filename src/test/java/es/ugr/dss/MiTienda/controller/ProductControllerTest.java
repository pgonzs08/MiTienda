package es.ugr.dss.MiTienda.controller;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /catalog debe cargar la vista 'products' con el listado de productos")
    void getProductsPage_DeberiaDevolverVistaProductsConModelo() throws Exception {
        // Arrange
        Product producto = new Product();
        given(productService.getAllProducts()).willReturn(List.of(producto));

        // Act & Assert
        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", List.of(producto)));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("GET /catalog/add debe mostrar la vista 'product_form' con un nuevo objeto Product")
    void showAddForm_DeberiaDevolverVistaFormularioNuevoProducto() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/catalog/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product_form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("GET /catalog/edit/{id} debe mostrar el formulario relleno con los datos del producto existente")
    void editForm_DeberiaDevolverVistaFormularioConProductoExistente() throws Exception {
        // Arrange
        Long productId = 1L;
        Product productoExistente = new Product();
        given(productService.getProductById(productId)).willReturn(productoExistente);

        // Act & Assert
        mockMvc.perform(get("/catalog/edit/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product_form"))
                .andExpect(model().attribute("product", productoExistente));

        verify(productService).getProductById(productId);
    }

    @Test
    @DisplayName("POST /catalog/add debe guardar el producto y redirigir a /admin")
    void addProduct_DeberiaGuardarProductoYRedirigirAAdmin() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/catalog/add")
                        .param("name", "Teclado Mecánico")
                        .param("price", "49.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    @DisplayName("POST /catalog/update/{id} debe actualizar el producto si existe y redirigir a /admin")
    void editProduct_DeberiaActualizarYRedirigir_CuandoProductoExiste() throws Exception {
        // Arrange
        Long productId = 1L;
        Product productoExistente = new Product();
        given(productService.getProductById(productId)).willReturn(productoExistente);

        // Act & Assert
        mockMvc.perform(post("/catalog/update/{id}", productId)
                        .param("name", "Ratón Gaming")
                        .param("price", "29.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).saveProduct(productoExistente);
    }

    @Test
    @DisplayName("POST /catalog/update/{id} no debe llamar a saveProduct si el producto no existe")
    void editProduct_NoDeberiaGuardar_CuandoProductoNoExiste() throws Exception {
        // Arrange
        Long productId = 99L;
        given(productService.getProductById(productId)).willReturn(null);

        // Act & Assert
        mockMvc.perform(post("/catalog/update/{id}", productId)
                        .param("name", "Producto Inexistente")
                        .param("price", "10.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService, never()).saveProduct(any());
    }

    @Test
    @DisplayName("POST /catalog/delete/{id} debe eliminar el producto y redirigir a /admin")
    void deleteProduct_DeberiaEliminarYRedirigirAAdmin() throws Exception {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        mockMvc.perform(post("/catalog/delete/{id}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).deleteProduct(productId);
    }
}