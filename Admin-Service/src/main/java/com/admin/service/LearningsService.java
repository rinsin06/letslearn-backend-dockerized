package com.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Cart;
import com.admin.entity.Course;
import com.admin.entity.Learnings;
import com.admin.entity.Wishlist;
import com.admin.repository.LearningsRepository;
import com.admin.repository.WishListRepository;

import jakarta.transaction.Transactional;

@Service
public class LearningsService {
	
	@Autowired
	LearningsRepository repository;
	
	@Autowired
	CourseService courseService;
	
	
	public Learnings addToLearnings(Long userId,Course course) {
		
		
		Learnings newItem = new Learnings();
		
		newItem.setCourse(course);
		
		newItem.setUserId(userId);
		
		return repository.save(newItem);
		
		
	}
	
	public String recordProgress(Long userId,Long courseId,Long progress) {
		
		
		
		 Optional<Learnings> learning = repository.findByUserIdAndCourse_CourseId(userId, courseId);
		    if (learning.isPresent()) {
		        Learnings updated = learning.get();
		        updated.setLessonProgress(progress);
		        repository.save(updated);
		        return "Progress Recorded";
		    } else {
		        // Handle the case where no record is found
		        return "No record found for the given user and course";
		    }
		
		
	}
	
	
	public String removeFromLearnings(Long id)
	{
		 repository.deleteById(id);
		 
		 
		 return "Successfully Deleted";
	}
	
	public List<Learnings> getListByUserId(Long id){
		
		
		return repository.findByUserId(id);
	}
	
	public String removeAll(List<Learnings> wishListItems) {
		
		 repository.deleteAll(wishListItems);
		 
		 return "WishList Cleared";
	}
	
public String addCoursesToLearnings(List<Long> coursesId,Long userId ) {

		
		for(Long courses:coursesId) {
			
			Learnings learnings = new Learnings();
			
			Optional<Course> course = courseService.findById(courses);
			
			learnings.setCourse(course.get());
			
			learnings.setUserId(userId);
			 
			
			repository.save(learnings);
			
			
		}
		
		return "Added to cart";
		
		
	}

@Transactional
public String deleteByCourseId(Long courseId) {
	
	repository.deleteByCourseCourseId(courseId);
	
	return"deleted";
}
}
