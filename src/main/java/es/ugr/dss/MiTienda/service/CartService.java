package es.ugr.dss.MiTienda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.ugr.dss.MiTienda.model.Product;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

@Component
@SessionScope
public class CartService {

    private final Map<Long, Integer> items = new HashMap<>();
    @Autowired
    private ProductService productService;

    public void addProduct(Long productId) {
        items.put(productId, items.getOrDefault(productId, 0) + 1);
    }

    public void removeProduct(Long productId) {
        items.remove(productId);
    }

    public Map<Product, Integer> getProductsInCart() {
        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
        	try {
                Product p = productService.getProductById(entry.getKey());
                products.put(p, entry.getValue());
        	}
            catch (EntityNotFoundException e) {
            	items.remove(entry.getKey());
            }
        }
        return Collections.unmodifiableMap(products);
    }
}