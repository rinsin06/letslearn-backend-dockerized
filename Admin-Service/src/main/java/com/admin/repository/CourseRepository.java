package com.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
	 Optional<Course> findByTitle(String title);
	 
	 Optional<Course> findById(Long id);
}
