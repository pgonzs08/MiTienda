package es.ugr.dss.MiTienda.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import es.ugr.dss.MiTienda.model.Product;
import es.ugr.dss.MiTienda.repository.ProductRepo;

@Service
public class ExportDatabaseService{
	
	private final ProductRepo productRepo;
	
	public ExportDatabaseService(ProductRepo repo) {
		this.productRepo = repo;
	}
	
	public byte[] exportDatabaseToSql() {
        List<Product> products = productRepo.findAll();
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("-- Script de exportación de productos\n");
        sqlBuilder.append("-- Generado automáticamente\n\n");

        for (Product product : products) {
            // Escapar comillas simples en el nombre para evitar errores en SQL (ej: "L'Oreal" -> "L''Oreal")
            String sanitizedName = product.getName() != null 
                    ? product.getName().replace("'", "''") 
                    : "";

            sqlBuilder.append(String.format(
                "INSERT INTO product (id, name, price) VALUES (%d, '%s', %.2f);\n",
                product.getId(),
                sanitizedName,
                product.getPrice()
            ).replace(",", ".")); // Asegurar el formato decimal con punto (ej: 19.99)
        }

        // Convertir la cadena SQL compilada a un array de bytes en codificación UTF-8
        return sqlBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
	
}