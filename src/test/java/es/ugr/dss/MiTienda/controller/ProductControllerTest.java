package es.ugr.dss.MiTienda.controller;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /catalog debe cargar la vista 'products' con el listado de productos")
    @WithMockUser
    void getProductsPage_DeberiaDevolverVistaProductsConModelo() throws Exception {
        Product producto = new Product();
        given(productService.getFilteredProducts(null, null, null)).willReturn(List.of(producto));

        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", List.of(producto)));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("GET /admin/catalog/add debe mostrar la vista 'product_form'")
    @WithMockUser
    void showAddForm_DeberiaDevolverVistaFormularioNuevoProducto() throws Exception {
        mockMvc.perform(get("/admin/catalog/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("product_form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("GET /admin/catalog/edit/{id} debe mostrar el formulario con los datos del producto")
    @WithMockUser
    void editForm_DeberiaDevolverVistaFormularioConProductoExistente() throws Exception {
        Long productId = 1L;
        Product productoExistente = new Product();
        given(productService.getProductById(productId)).willReturn(productoExistente);

        mockMvc.perform(get("/admin/catalog/edit/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product_form"))
                .andExpect(model().attribute("product", productoExistente));

        verify(productService).getProductById(productId);
    }

    @Test
    @DisplayName("POST /admin/catalog/add debe guardar el producto y redirigir a /admin")
    @WithMockUser
    void addProduct_DeberiaGuardarProductoYRedirigirAAdmin() throws Exception {
        mockMvc.perform(post("/admin/catalog/add")
                        .with(csrf()) // Necesario cuando Spring Security está presente
                        .param("name", "Teclado Mecánico")
                        .param("price", "49.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    @DisplayName("POST /admin/catalog/update/{id} debe actualizar el producto si existe")
    @WithMockUser
    void editProduct_DeberiaActualizarYRedirigir_CuandoProductoExiste() throws Exception {
        Long productId = 1L;
        Product productoExistente = new Product();
        given(productService.getProductById(productId)).willReturn(productoExistente);

        mockMvc.perform(post("/admin/catalog/update/{id}", productId)
                        .with(csrf())
                        .param("name", "Ratón Gaming")
                        .param("price", "29.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).saveProduct(productoExistente);
    }

    @Test
    @DisplayName("POST /admin/catalog/update/{id} no debe guardar si el producto no existe")
    @WithMockUser
    void editProduct_NoDeberiaGuardar_CuandoProductoNoExiste() throws Exception {
        Long productId = 99L;
        given(productService.getProductById(productId)).willReturn(null);

        mockMvc.perform(post("/admin/catalog/update/{id}", productId)
                        .with(csrf())
                        .param("name", "Inexistente")
                        .param("price", "10.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService, never()).saveProduct(any());
    }

    @Test
    @DisplayName("POST /admin/catalog/delete/{id} debe eliminar el producto y redirigir a /admin")
    @WithMockUser
    void deleteProduct_DeberiaEliminarYRedirigirAAdmin() throws Exception {
        Long productId = 1L;

        mockMvc.perform(post("/admin/catalog/delete/{id}", productId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(productService).deleteProduct(productId);
    }
}