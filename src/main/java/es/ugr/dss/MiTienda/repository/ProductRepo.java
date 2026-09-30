package es.ugr.dss.MiTienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ugr.dss.MiTienda.model.Product;

public interface ProductRepo extends JpaRepository<Product, Long>{

}

