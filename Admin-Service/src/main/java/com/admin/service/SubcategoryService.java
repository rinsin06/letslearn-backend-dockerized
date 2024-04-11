package com.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Category;
import com.admin.entity.Subcategory;
import com.admin.repository.SubcategoryRepository;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public Subcategory createSubcategory(String subcategory,Category category) {
    	Subcategory newSubCategory = new Subcategory();
    	
    	newSubCategory.setName(subcategory);
    	
    	newSubCategory.setCategory(category);
   
    	
        return subcategoryRepository.save(newSubCategory);
    }
    
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategoryCategoryId(categoryId);
    }
    
 public Subcategory getByName(String name) {
    	
    	Optional<Subcategory> subCategory =subcategoryRepository.findByName(name);
    	
    	return subCategory.get();
    }

}
