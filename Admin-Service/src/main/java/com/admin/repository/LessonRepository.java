package com.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.admin.entity.Course;
import com.admin.entity.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
	
	
	 List<Lesson> findByCourseId(Long courseId);
	 
	 void deleteByCourseId(Long courseId);

}
