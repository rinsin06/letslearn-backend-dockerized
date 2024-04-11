package com.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.entity.Learnings;
import com.admin.entity.Wishlist;

public interface LearningsRepository extends JpaRepository<Learnings, Long> {
	
	 
	List<Learnings> findByUserId(Long userId);
	
	 Optional<Learnings> findByUserIdAndCourse_CourseId(Long userId, Long courseId);
	
	 void deleteByCourseCourseId(Long courseId);
}
