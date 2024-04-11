package com.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Cart;
import com.admin.entity.Course;
import com.admin.repository.CartRepository;

import jakarta.transaction.Transactional;


@Service
public class CartService {
	
	@Autowired
	CourseService courseService;
	
	
	@Autowired
	CartRepository repository;
	
	
	public String addtocart(List<Long> coursesId,Long userId ) {
		
		
		
		
		for(Long courses:coursesId) {
			
			Cart newCart = new Cart();
			
			Optional<Course> course = courseService.findById(courses);
			
			newCart.setCourse(course.get());
			
			newCart.setUserId(userId);
			 
			
			repository.save(newCart);
			
			
		}
		
		return "Added to cart";
		
		
	}
	
	public List<Cart> getUserCart(Long userId){
		
		
		return repository.findByUserId(userId);
	}
	
	
	public String removefromcart(Long id) {
		
		repository.deleteById(id);
		
		
		return "Removed from cart";
		
	}
	
	public String removeAll(List<Cart> cartItems) {
		
		
		repository.deleteAll(cartItems);
		
		
		return "Cart cleared";
		
		
	}
	
	@Transactional
public String deleteByCourseId(Long courseId) {
		
		repository.deleteByCourseCourseId(courseId);
		
		return"deleted";
	}

}
