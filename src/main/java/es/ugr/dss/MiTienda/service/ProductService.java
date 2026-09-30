package es.ugr.dss.MiTienda.service;

import org.springframework.stereotype.Service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;

import java.util.List;

@Service
public class ProductService{

	private final ProductRepo repository;

	public ProductService(ProductRepo repository) {
		this.repository = repository;
	}

	public List<Product> getAllProducts(){
		return this.repository.findAll();
	}

	public Product getProductById(Long id){
		return this.repository.getReferenceById(id);
	}

	public void saveProduct(Product product) {
		this.repository.save(product);
	}

	public void deleteProduct(Long id){
		this.repository.deleteById(id);
	}
}

