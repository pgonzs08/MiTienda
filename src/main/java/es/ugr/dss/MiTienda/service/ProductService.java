package es.ugr.dss.MiTienda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class ProductService{

	@Autowired
	private ProductRepo repository;

	public List<Product> getAllProducts(){
		return this.repository.findAll();
	}

	public List<Product> getFilteredProducts(String name, Double min, Double max){
		
		String cleanName = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
		
		return this.repository.buscarPorFiltros(cleanName, min, max);
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

