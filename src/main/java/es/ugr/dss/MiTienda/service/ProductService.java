package es.ugr.dss.MiTienda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;

import java.util.List;

@Service
public class ProductService{

	@Autowired
	private ProductRepo repository;

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

