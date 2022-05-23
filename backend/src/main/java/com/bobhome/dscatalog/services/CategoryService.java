package com.bobhome.dscatalog.services;

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
import com.bobhome.dscatalog.entities.Category;
import com.bobhome.dscatalog.repositories.CategoryRepository;
import com.bobhome.dscatalog.services.exceptions.DataBaseException;
import com.bobhome.dscatalog.services.exceptions.ResourceNotFoundException;


 
@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable){
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada!"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
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

	
	
		/*@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		
		List<CategoryDTO> listDto = new ArrayList<>();
		for(Category cat : list) {
			listDto.add(new CategoryDTO(cat));
		}
		return listDto;
	} */

}
