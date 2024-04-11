package com.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.entity.Course;
import com.admin.entity.Wishlist;

public interface WishListRepository extends JpaRepository<Wishlist, Long> {
	
	 
	List<Wishlist> findByUserId(Long userId);
	
	 void deleteByCourseCourseId(Long courseId);
}