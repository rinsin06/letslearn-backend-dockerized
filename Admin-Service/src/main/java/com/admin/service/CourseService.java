package com.admin.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.admin.dto.courseRequest;
import com.admin.entity.Category;
import com.admin.entity.Course;
import com.admin.entity.Lesson;
import com.admin.entity.Subcategory;
import com.admin.repository.CourseRepository;
import com.admin.repository.LessonRepository;

import jakarta.transaction.Transactional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
  
    private static long lessonIdCounter = 0;
    
    
    @Transactional
    public Course createCourse(courseRequest courseRequest,Category category,Subcategory subCategory) {
    	
    	
    	if(courseRequest.getCourseId() !=null) {
    		
    		Optional<Course> exists = courseRepository.findById(courseRequest.getCourseId());
    	
    	
    	
    	Course existingCourse = exists.get();
    	if(exists.isPresent()) {

            
existingCourse.setAuthorName(courseRequest.getAuthorName());
        	
    		existingCourse.setCategory(category);
        	
    		existingCourse.setSubcategory(subCategory);
        	
        	
    		existingCourse.setCoverImage(courseRequest.getImageUrl());
    		
        	
    		existingCourse.setTitle(courseRequest.getTitle());
        	
    		existingCourse.setCreationDate(courseRequest.getCreationDate());
        	
    		existingCourse.setDescription(courseRequest.getDescription());
        	
    		existingCourse.setDuration(courseRequest.getDuration());
        	
    		existingCourse.setPrice(courseRequest.getPrice());
        	
    		
        	
            return courseRepository.save(existingCourse);
    		
    		
    		
    	}else {
    		
    		Course course = new Course();
        	
        	course.setAuthorName(courseRequest.getAuthorName());
        	
        	course.setCategory(category);
        	
        	course.setSubcategory(subCategory);
        	
        	
    		course.setCoverImage(courseRequest.getImageUrl());
    		
        	
        	course.setTitle(courseRequest.getTitle());
        	
        	course.setCreationDate(courseRequest.getCreationDate());
        	
        	course.setDescription(courseRequest.getDescription());
        	
        	course.setDuration(courseRequest.getDuration());
        	
        	course.setPrice(courseRequest.getPrice());
        	
        	
        	
        	
            return courseRepository.save(course);
    		
    	
        	
    		
    	}
    
    }else {
    	
    	Course course = new Course();
    	
    	course.setAuthorName(courseRequest.getAuthorName());
    	
    	course.setCategory(category);
    	
    	course.setSubcategory(subCategory);
    	
    	
		course.setCoverImage(courseRequest.getImageUrl());
		
    	
    	course.setTitle(courseRequest.getTitle());
    	
    	course.setCreationDate(courseRequest.getCreationDate());
    	
    	course.setDescription(courseRequest.getDescription());
    	
    	course.setDuration(courseRequest.getDuration());
    	
    	course.setPrice(courseRequest.getPrice());
    	
    	
    	
        return courseRepository.save(course);
		
	
    }
    }
    
    public List<Course> allCourses(){
    	
    	return courseRepository.findAll();
    }
    
    public Optional<Course> findByTitle(String title)
    {
    	return courseRepository.findByTitle(title);
    }
    
    public Optional<Course> findById(Long id)
    {
    	return courseRepository.findById(id);
    }
    
   
    public String deleteById(Long courseId) {
    	
    	courseRepository.deleteById(courseId);
    	
    	return "Course Deleted";
    }
}
