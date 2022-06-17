package com.bobhome.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bobhome.dscatalog.dto.CategoryDTO;
import com.bobhome.dscatalog.dto.ProductDTO;
import com.bobhome.dscatalog.entities.Category;
import com.bobhome.dscatalog.entities.Product;
import com.bobhome.dscatalog.repositories.CategoryRepository;
import com.bobhome.dscatalog.repositories.ProductRepository;
import com.bobhome.dscatalog.services.exceptions.DataBaseException;
import com.bobhome.dscatalog.services.exceptions.ResourceNotFoundException;


 
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable){
		List<Category> categories = (categoryId == 0) ? null : 
					Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> list = repository.find(categories, name.trim(), pageable);
		return list.map(x -> new ProductDTO(x));
	}
	
	/*@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
	 	List<Category> categories = (categoryId == 0) ? null :
	Arrays.asList(categoryRepository.getOne(categoryId));
	 	Page<Product> list = repository.find(categories, name, pageable);
	 	return list.map(x -> new ProductDTO(x));
	}*/

	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada!"));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
		
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado!" + id);
		}
	}

	//Não utilizar @Transactional, pois ele não deixar capturar uma exceção específica.
	public void delete(Long id) {
		try{
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found!" + id);
		}
		catch(DataAccessException e) {
			throw new DataBaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setMoment(dto.getMoment());	
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}

	
	
		/*@Transactional(readOnly = true)
	public List<ProductDTO> findAll(){
		List<Product> list = repository.findAll();
		
		List<ProductDTO> listDto = new ArrayList<>();
		for(Product cat : list) {
			listDto.add(new ProductDTO(cat));
		}
		return listDto;
	} */

}
