package com.bobhome.dscatalog.repositories;



import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.bobhome.dscatalog.entities.Product;
import com.bobhome.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repo;
	
	private long existingId;
	private long doesExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		doesExistingId = 1000L;
		countTotalProducts = 25L; 
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repo.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts +1 , product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repo.deleteById(existingId);
		
		Optional<Product> result  = repo.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrownEmptyResultDataAccessExceptionWhenDoesNotExist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repo.deleteById(doesExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> result  = repo.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> result  = repo.findById(doesExistingId);
		Assertions.assertTrue(result.isEmpty());
	}
}
