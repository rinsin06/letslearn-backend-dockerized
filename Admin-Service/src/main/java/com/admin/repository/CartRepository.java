package com.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.entity.Cart;
import com.admin.entity.Category;

public interface CartRepository extends JpaRepository<Cart, Long> {

	 List<Cart> findByUserId(Long userId);
	
	 void deleteByCourseCourseId(Long courseId);
}
