package es.ugr.dss.MiTienda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ugr.dss.MiTienda.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{
	@Query("SELECT p FROM Product p WHERE " +
	           "(:query IS NULL OR :query = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
	           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
	           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> buscarPorFiltros(
        @Param("query") String nombre, 
        @Param("minPrice") Double precioMin, 
        @Param("maxPrice") Double precioMax
    );
}

