package es.ugr.dss.MiTienda.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("productoId") 
    private Long id;

	@JsonProperty("productoNombre") 
    private String name;
	
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(this.hashCode(), product.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    public Long getId() {
    	return this.id;
    }

    public String getName() {
		return this.name;
	}
    
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPrice() {
		return this.price;
	}
    
	public void setPrice(double price) {
		this.price = price;
	}


}
