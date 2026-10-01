package es.ugr.dss.MiTienda.model;

import es.ugr.dss.MiTienda.repository.ProductRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // Carga application-test.properties
class ProductTest {

    @Autowired
    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        // Se ejecuta antes de CADA test
        Product p1 = new Product();
        
        p1.setName("Portátil");
        p1.setPrice(450.00);
        
        Product p2 = new Product();
        
        p2.setName("Teclado");
        p2.setPrice(135.00);

        productRepo.saveAll(List.of(p1, p2));
    }
    
    @Test
    @DisplayName("Getters, Setters, Equals de Product")
    void testProduct() {
        Optional<Product> p1 = productRepo.findById((long)1);
        Optional<Product> p1_2 = productRepo.findById((long)1);
        Optional<Product> p2 = productRepo.findById((long)2);

        assertThat(p1).isPresent();
        assertThat(p2).isPresent();
        
        assertThat(p1.get().getName()).isEqualTo("Portátil");
        assertThat(p1.get().getPrice()).isEqualTo(450.00);
        
        assertThat(p2.get().getName()).isEqualTo("Teclado");
        assertThat(p2.get().getPrice()).isEqualTo(135.00);
        
        assertThat(p1.get()).isEqualTo(p1_2.get());
        assertThat(p1.get()).isNotEqualTo(p2.get());
        
    }

}