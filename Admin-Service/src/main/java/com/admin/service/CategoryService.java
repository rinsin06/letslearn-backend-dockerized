package com.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Category;
import com.admin.repository.CategoryRepository;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(String category) {
    	
    	Category newCategory = new Category();
    	
    	newCategory.setName(category);
        return categoryRepository.save(newCategory);
    }
    
    public List<Category> allCategories()
    {
    	return categoryRepository.findAll();
    }
    
    public Category getByName(String name) {
    	
    	Optional<Category> category = categoryRepository.findByName(name);
    	
    	return category.get();
    }
}
