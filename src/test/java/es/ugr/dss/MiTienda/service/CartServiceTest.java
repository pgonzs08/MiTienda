package es.ugr.dss.MiTienda.service;

import es.ugr.dss.MiTienda.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("addProduct incrementa la cantidad del producto en el carrito")
    void addProduct_DeberiaAnadirEIncrementarCantidad() {

        Long productId = 1L;
        Product mockProduct = new Product();
        given(productService.getProductById(productId)).willReturn(mockProduct);

        cartService.addProduct(productId);
        cartService.addProduct(productId);

        Map<Product, Integer> cart = cartService.getProductsInCart();

        assertThat(cart).hasSize(1);
        assertThat(cart.get(mockProduct)).isEqualTo(2);
    }

    @Test
    @DisplayName("removeProduct elimina el producto del carrito")
    void removeProduct_DeberiaEliminarProductoDelCarrito() {

        Long productId = 1L;
        cartService.addProduct(productId);

        cartService.removeProduct(productId);
        Map<Product, Integer> cart = cartService.getProductsInCart();

        assertThat(cart).isEmpty();
    }

    @Test
    @DisplayName("getProductsInCart ignora productos cuando ProductService devuelve null")
    void getProductsInCart_DeberiaIgnorarProductosNulos() {

        Long idExistente = 1L;
        Long idInexistente = 2L;

        Product mockProduct = new Product();

        given(productService.getProductById(idExistente)).willReturn(mockProduct);
        given(productService.getProductById(idInexistente)).willReturn(null);

        cartService.addProduct(idExistente);
        cartService.addProduct(idInexistente);

        Map<Product, Integer> cart = cartService.getProductsInCart();

        assertThat(cart).hasSize(1);
        assertThat(cart).containsKey(mockProduct);
        assertThat(cart.get(mockProduct)).isEqualTo(1);
    }

    @Test
    @DisplayName("getProductsInCart devuelve un mapa inmodificable")
    void getProductsInCart_DeberiaDevolverMapaInmodificable() {

        Long productId = 1L;
        Product mockProduct = new Product();
        given(productService.getProductById(productId)).willReturn(mockProduct);

        cartService.addProduct(productId);
        Map<Product, Integer> cart = cartService.getProductsInCart();

        assertThatThrownBy(() -> cart.put(mockProduct, 5))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}