package es.ugr.dss.MiTienda.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.service.CartService;
import es.ugr.dss.MiTienda.service.ProductService;

@WebMvcTest(controllers = CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;
    
    @MockitoBean
    private ProductService productService;


    @Test
    @DisplayName("GET /cart debe cargar la vista 'cart' con el contenido del carrito")
    @WithMockUser
    void cart_DeberiaDevolverVistaCartConElementos() throws Exception {
        // Arrange
        Product producto = new Product();
        Map<Product, Integer> cartItems = new HashMap<>();
        cartItems.put(producto, 2);

        given(cartService.getProductsInCart()).willReturn(cartItems);

        // Act & Assert
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attribute("cartItems", cartItems));

        verify(cartService).getProductsInCart();
    }

    @Test
    @DisplayName("POST /cart/add/{id} debe añadir un producto y devolver status OK")
    @WithMockUser
    void addToCart_DeberiaAnadirProductoYOK() throws Exception {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        mockMvc.perform(post("/cart/add/{id}", productId)
                        .with(csrf())) // Incluye el token CSRF para peticiones POST
                .andExpect(status().isOk());

        verify(cartService).addProduct(productId);
    }
    
    @Test
    @DisplayName("POST /cart/undo/{id} debe eliminar un producto y devolver status OK")
    @WithMockUser
    void addToCartUndo_DeberiaEliminarProductoYOK() throws Exception {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        mockMvc.perform(post("/cart/undo/{id}", productId)
                        .with(csrf())) // Incluye el token CSRF para peticiones POST
                .andExpect(status().isOk());

        verify(cartService).removeProduct(productId);
    }

    @Test
    @DisplayName("POST /cart/remove/{id} debe eliminar un producto y redirigir a /cart")
    @WithMockUser
    void removeFromCart_DeberiaEliminarProductoYRedirigirACart() throws Exception {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        mockMvc.perform(post("/cart/remove/{id}", productId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeProduct(productId);
    }
}