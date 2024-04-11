package com.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.entity.Course;
import com.admin.entity.Wishlist;
import com.admin.repository.WishListRepository;

import jakarta.transaction.Transactional;


@Service
public class WishListService {
	
	@Autowired
	WishListRepository repository;
	
	
	public Wishlist addToWishList(Long userId,Course course) {
		
		
		Wishlist newItem = new Wishlist();
		
		newItem.setCourse(course);
		
		newItem.setUserId(userId);
		
		return repository.save(newItem);
		
		
	}
	
	
	public String removeFromWishList(Long id)
	{
		 repository.deleteById(id);
		 
		 
		 return "Successfully Deleted";
	}
	
	public List<Wishlist> getListByUserId(Long id){
		
		
		return repository.findByUserId(id);
	}
	
	public String removeAll(List<Wishlist> wishListItems) {
		
		 repository.deleteAll(wishListItems);
		 
		 return "WishList Cleared";
	}
	
	@Transactional
	public String deleteByCourseId(Long courseId) {
		
		repository.deleteByCourseCourseId(courseId);
		
		return"deleted";
	}
}
