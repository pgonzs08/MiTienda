package es.ugr.dss.MiTienda.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.ugr.dss.MiTienda.service.ExportDatabaseService;

@RestController
@RequestMapping("/admin")
public class ExportController{

	private final ExportDatabaseService exportService;

	ExportController(ExportDatabaseService exportService) {
		this.exportService = exportService;
	}

	@GetMapping("/download-db-sql")
	ResponseEntity<byte[]> downloadDatabaseSql() {
		byte[] sqlData = exportService.exportDatabaseToSql();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"products.sql\"")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(sqlData);
	}
}