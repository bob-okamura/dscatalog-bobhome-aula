package com.bobhome.dscatalog.tests;

import java.time.Instant;

import com.bobhome.dscatalog.dto.ProductDTO;
import com.bobhome.dscatalog.entities.Category;
import com.bobhome.dscatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L, "Xiaomi", "Se é da China, é bom", 500.00, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg", Instant.parse("2020-07-13T20:50:07.12345Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L, "Electronics");
	}

}
