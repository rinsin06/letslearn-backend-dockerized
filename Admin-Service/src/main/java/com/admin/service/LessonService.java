package com.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dto.lessonRequest;
import com.admin.entity.Category;
import com.admin.entity.Course;
import com.admin.entity.Lesson;
import com.admin.repository.CategoryRepository;
import com.admin.repository.CourseRepository;
import com.admin.repository.LessonRepository;

import jakarta.transaction.Transactional;

@Service
public class LessonService {
	
	@Autowired
	LessonRepository lessonRepository;
	
	
	@Autowired
	CourseService courseService;
	
	
	
	public Lesson createLesson(lessonRequest lesson, Long courseId) {
		
		
if(lesson.getId() !=null) {
    		
	 Optional<Lesson> existingLessonOptional = lessonRepository.findById(lesson.getId());
     Optional<Course> courseOptional = courseService.findById(courseId);
    	
    	
    	
     if (existingLessonOptional.isPresent() && courseOptional.isPresent()) {
         Lesson existingLesson = existingLessonOptional.get();
         Course course = courseOptional.get();

         // Update the existing lesson's properties
         existingLesson.setTitle(lesson.getTitle());
         existingLesson.setContent(lesson.getContent());
         existingLesson.setCourseId(courseId);
         existingLesson.setVideoUrl(lesson.getVideoUrl());
         existingLesson.setDescription(lesson.getDescription());
         existingLesson.setLessonOrder(lesson.getLessonOrder());

         return lessonRepository.save(existingLesson);
    		
    		
    	}else {
    		
    		

    	        if (courseOptional.isPresent()) {
    	            Course course = courseOptional.get();
    	            Lesson newLesson = new Lesson();
    	            newLesson.setTitle(lesson.getTitle());
    	            newLesson.setContent(lesson.getContent());
    	            newLesson.setCourseId(courseId);
    	            newLesson.setVideoUrl(lesson.getVideoUrl());
    	            newLesson.setDescription(lesson.getDescription());
    	            newLesson.setLessonOrder(lesson.getLessonOrder());

    	            return lessonRepository.save(newLesson);}
    		
    	
        	
    		
    	}
    
    }else {
    	
    	Optional<Course> courseOptional = courseService.findById(courseId);
    	
    	  if (courseOptional.isPresent()) {
	            Course course = courseOptional.get();
	            Lesson newLesson = new Lesson();
	            newLesson.setTitle(lesson.getTitle());
	            newLesson.setContent(lesson.getContent());
	            newLesson.setCourseId(courseId);
	            newLesson.setVideoUrl(lesson.getVideoUrl());
	            newLesson.setDescription(lesson.getDescription());
	            newLesson.setLessonOrder(lesson.getLessonOrder());

	            return lessonRepository.save(newLesson);
	            }
		
	
  	
		
	
    }
return null;
	}

	    public List<Lesson> allCategories()
	    {
	    	return lessonRepository.findAll();
	    }
	    
	   public List<Lesson> getByCourse(String title){
		   
		   Optional<Course> Course = courseService.findByTitle(title);
		   
		   return lessonRepository.findByCourseId(Course.get().getCourseId());
	   }
	   
public List<Lesson> getByCourseId(Long courseId){
		   
		  
		   
		   return lessonRepository.findByCourseId(courseId);
	   }

@Transactional
public String deleteById(Long lessonId) {
	
	lessonRepository.deleteById(lessonId);
	
	return "Lesson Deleted";
}

@Transactional
public String deleteByCourseId(Long courseId) {
	
	lessonRepository.deleteByCourseId(courseId);
	
	return "Lessons Deleted";
}

}
