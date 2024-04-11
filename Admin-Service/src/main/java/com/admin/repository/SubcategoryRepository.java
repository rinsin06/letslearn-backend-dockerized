package com.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.admin.entity.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
	
	List<Subcategory> findByCategoryCategoryId(Long categoryId);
	
	 Optional<Subcategory> findByName(String name);
}
