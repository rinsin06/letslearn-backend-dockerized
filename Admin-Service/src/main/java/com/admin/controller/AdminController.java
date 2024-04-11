package com.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.admin.dto.ImageRequest;
import com.admin.dto.cartRequest;
import com.admin.dto.categoryRequest;
import com.admin.dto.courseRequest;
import com.admin.dto.courseWithImageRequest;
import com.admin.dto.lessonRequest;
import com.admin.dto.lessonWithCourseRequest;
import com.admin.dto.progressRequest;
import com.admin.dto.singleCartRequest;
import com.admin.dto.subCategoryRequest;
import com.admin.dto.wishListRemoveRequest;
import com.admin.dto.wishListRequest;
import com.admin.entity.Cart;
import com.admin.entity.Category;
import com.admin.entity.Coupon;
import com.admin.entity.Course;
import com.admin.entity.Learnings;
import com.admin.entity.Lesson;
import com.admin.entity.Subcategory;
import com.admin.entity.Wishlist;
import com.admin.repository.CartRepository;
import com.admin.repository.CourseRepository;
import com.admin.repository.WishListRepository;
import com.admin.service.CartService;
import com.admin.service.CategoryService;
import com.admin.service.CouponService;
import com.admin.service.CouponUsageService;
import com.admin.service.CourseService;
import com.admin.service.LearningsService;
import com.admin.service.LessonService;
import com.admin.service.SubcategoryService;
import com.admin.service.WishListService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;




@RestController
@RequestMapping("/admin")
public class AdminController {
	
	 @Autowired
	 private CourseService courseService;
	 
	 @Autowired
	 LessonService lessonService;
	 
	 @Autowired
	 WishListService wishListService;
	 
	 
	 @Autowired 
	private CartService cartService;
	
	@Autowired
    private SubcategoryService subcategoryService;
	
	 @Autowired
	    private CategoryService categoryService;
	
	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private LearningsService learningService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CouponUsageService couponUsageService;
	
	 @Value("${rzp_key_id}")
	    private String keyId;

	    @Value("${rzp_key_secret}")
	    private String secret;
	
		 @GetMapping("/dashboard")
		 public String dashboard() {
		    	
		        return "Welcome";
		    }
		 
		 @GetMapping("/all-categories")
		 public ResponseEntity<List<Map<String, Object>>> getCategoriesWithSubcategories() {
		        List<Category> categories = categoryService.allCategories();

		        List<Map<String, Object>> response = categories.stream()
		                .map(category -> {
		                    Map<String, Object> categoryMap = new HashMap<>();
		                    categoryMap.put("category", category);
		                    categoryMap.put("subcategories", subcategoryService.getSubcategoriesByCategoryId(category.getCategoryId())); // Use id instead of getCategoryID
		                    return categoryMap;
		                })
		                .collect(Collectors.toList());

		        return ResponseEntity.ok(response);
		    }
		 
		 @GetMapping("/all-courses")
		 public ResponseEntity<List<Course>> getCourses()
		 {
			 List<Course> courses = courseService.allCourses();
			 
			 
			 return new ResponseEntity<>(courses, HttpStatus.OK);
			 
		 }
		 @PostMapping("/create-course")
		    public ResponseEntity<Course> createCourse(@RequestBody courseRequest request) throws IOException {
			 
		
		        Category category = categoryService.getByName(request.getCategory());
		        
		        Subcategory subCategory = subcategoryService.getByName(request.getSubcategory());
		        
		        Course createdCourse = courseService.createCourse(request,category,subCategory);
		        
		        
		        
		        
		        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
		    }
		 
		 @GetMapping("/delete-course")
		 public ResponseEntity<String> deleteCourse(@RequestParam(value="course_id") Long courseId){
			 

			 lessonService.deleteByCourseId(courseId);
			
			 
			 learningService.deleteByCourseId(courseId);
			 
			 wishListService.deleteByCourseId(courseId);
			 
			 cartService.deleteByCourseId(courseId);
			  courseService.deleteById(courseId);
			 


				
			 
			 return new ResponseEntity<>("Course Delete", HttpStatus.OK);
			 
			 
		 }
		 
		 @PostMapping("/addTo-wishlist")
		 public ResponseEntity<Wishlist> addToWishList(@RequestBody wishListRequest request) throws IOException{
			 
			 Optional<Course> course = courseService.findById(request.getCourseId());
			 
			 Wishlist newWhishList = wishListService.addToWishList(request.getUserId(), course.get());
			 
			 return new ResponseEntity<>(newWhishList, HttpStatus.CREATED);
			 
			 
		 }
		 
		 @GetMapping("/existsInWishList")
		 public ResponseEntity<String> existsInWishList(@RequestParam(value="courseId")Long courseId){
			 
			List< Wishlist >existing = wishListService.getListByUserId(courseId);
			 
			 if(existing == null) {
				 
				 return new ResponseEntity<>("Not In the List", HttpStatus.OK);
			 }else {
				 
				 return new ResponseEntity<>("Exists In the List", HttpStatus.OK);
			 }
		 }
		 
		 
		 @GetMapping("/getWishList")
		 public ResponseEntity<List<Wishlist>> getWhisList(@RequestParam(value="user_id") Long userId)
		 {
			 
			 List<Wishlist> wishList = wishListService.getListByUserId(userId);
			 
			 return new ResponseEntity<>(wishList,HttpStatus.OK);
		 }
		 
		 
		 
		 
		 @GetMapping("/removeFromWishList")
		 public ResponseEntity<String> removeFromWishList(@RequestParam(value="id")Long id)throws IOException{
			 
			 
			 try {
				wishListService.removeFromWishList(id);
				 
				 return new ResponseEntity<>("Removed",HttpStatus.OK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				 return new ResponseEntity<>("Not Removed",HttpStatus.OK);
			}
		 }
		 
		 	
		 	@PostMapping("/checkoutFromWishList")
		 	public ResponseEntity<String> checkoutFromWishList(@RequestBody wishListRemoveRequest request){
		 		
		 		List<Wishlist> wishListItems = new ArrayList<>();
		 		
		 		List<Long> ids = request.getWishListIds();
		 		
		 		for(Long id:ids ) {
		 			
		 			
		 			Optional<Wishlist> item = wishListRepository.findById(id);
		 			
		 			
		 			wishListItems.add(item.get());
		 			
		 		}
		 		
		 		
		 		
		 		
		 		
		 		String message = wishListService.removeAll(wishListItems);
		 		
		 		return new ResponseEntity<>(message, HttpStatus.OK);
		 		
		 	}
		 
		 
		 
//		 @GetMapping('/delete-course')
//		 public ResponeEntity<String> deleteCourse(@RequestParam(value='courseId') Long courseId){
//			 
//			 
//			 
//		 }
		 @PostMapping("/create-category")
		    public ResponseEntity<Category> createCategory(@RequestBody categoryRequest category) {
		        Category createdCategory = categoryService.createCategory(category.getCategoryName());
		        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
		    }
		 
		 @GetMapping("/course-lessons")
		 public ResponseEntity<List<Lesson>> getLessons(@RequestParam(value="id") Long courseId){
			 
			 List<Lesson> courses = lessonService.getByCourseId(courseId);
			 
			 return new ResponseEntity<>(courses, HttpStatus.OK);
			 
			 
		 }
		 
		 @PostMapping("/create-lessons") 
		 public  ResponseEntity<List<Lesson>> createLessons(@RequestBody lessonWithCourseRequest request){
			 
			 List<lessonRequest> lessons = request.getLessonRequest();
			 
			 List<lessonRequest> deletedlessons = request.getDeleteRequest();
			 
			 Long courseId = request.getCourseId();
			 if(!deletedlessons.isEmpty()) {
				 
				 for (lessonRequest deletedLesson: deletedlessons) {
						
						lessonService.deleteById(deletedLesson.getId());
					}
				 
			 }
			
			 
			 List<Lesson> createdLessons = new ArrayList<>();
			    for (lessonRequest lesson : lessons) {
			        createdLessons.add(lessonService.createLesson(lesson,courseId));
			    }
			 
			 
			 return new ResponseEntity<>(createdLessons, HttpStatus.CREATED);
			 
			 
		 }
		 
		 
		 	@PostMapping("/create-subcategory")
		    public ResponseEntity<Subcategory> createSubcategory(@RequestBody subCategoryRequest subcategory) {
		 		
		 		Category category = categoryService.getByName(subcategory.getCategoryName());
		 		
		        Subcategory createdSubcategory = subcategoryService.createSubcategory(subcategory.getSubcategoryName(),category);
		        return new ResponseEntity<>(createdSubcategory, HttpStatus.CREATED);
		    }
		 	
		 	
		 	
		 	@PostMapping("/addtocart")
		    public ResponseEntity<String> addToCart(@RequestBody cartRequest request) {
		 		
		 		List<Long> courses = request.getCourseId();
		 		
		 		Long userId = request.getUserId();
		 		
		 		String message = cartService.addtocart(courses, userId);
		 		
		 		return new ResponseEntity<>(message, HttpStatus.CREATED);
		 		
		 	}
		 	
		 	
		 	@PostMapping("/addsingletocart")
		    public ResponseEntity<String> addOneToCart(@RequestBody singleCartRequest request) {
		 		
		 		Long courseId = request.getCourseId();
		 		
		 		Optional<Course> course = courseService.findById(courseId);
		 		
		 		Long userId = request.getUserId();
		 		
		 		Cart newItem = new Cart();
		 		
		 		newItem.setCourse(course.get());
		 		
		 		newItem.setUserId(userId);
		 		
		 		cartRepository.save(newItem);
		 		
		 		
		 		return new ResponseEntity<>("Added", HttpStatus.CREATED);
		 		
		 	}
		 	
		 	@GetMapping("/getUserCart")
		 	public ResponseEntity<List<Cart>> getCart(@RequestParam(value="user_id") Long userId)
		 	{
		 		
		 		List<Cart> cartItems = cartService.getUserCart(userId);
		 		
		 		return new ResponseEntity<>(cartItems, HttpStatus.OK);
		 	}
		 	
		 	
		 	@GetMapping("/removeCartItem")
		 	public ResponseEntity<String> removeCartItem(@RequestParam(value="id")Long id){
		 		
		 	String message = cartService.removefromcart(id);
		 		
		 		return new ResponseEntity<>(message, HttpStatus.OK);
		 	}
		 	
		 	
		 	@PostMapping("/checkoutFromCart")
		 	public ResponseEntity<String> checkoutFromCart(@RequestBody List<Cart> cartItems)
		 	{
		 		
		 		String message = cartService.removeAll(cartItems);
		 		
		 		return new ResponseEntity<>(message, HttpStatus.OK);
		 	}
		 	
		
			@PostMapping("/addCoursesToLearnings")
		    public ResponseEntity<String> addCoursesToLearnings(@RequestBody cartRequest request) {
		 		
		 		List<Long> courses = request.getCourseId();
		 		
		 		Long userId = request.getUserId();
		 		
		 		String message = learningService.addCoursesToLearnings(courses, userId);
		 		
		 		return new ResponseEntity<>(message, HttpStatus.CREATED);
		 		
		 	}
			
			@PostMapping("/addToLearnings")
		    public ResponseEntity<String> addToLearnings(@RequestBody singleCartRequest request) {
		 		
		 		Long courseId = request.getCourseId();
		 		
		 		Optional<Course> course = courseService.findById(courseId);
		 		
		 		Long userId = request.getUserId();
		 		
		 		Learnings message = learningService.addToLearnings(userId, course.get());
		 		
		 		return new ResponseEntity<>("Added", HttpStatus.CREATED);
		 		
		 	}
			
			 @GetMapping("/getLearnings")
			 public ResponseEntity<List<Learnings>> getLearnings(@RequestParam(value="user_id") Long userId)
			 {
				 
				 List<Learnings> learnings = learningService.getListByUserId(userId);
				 
				 return new ResponseEntity<>(learnings,HttpStatus.OK);
			 }
	
			 
			 @PostMapping("/recordProgress")
			 public ResponseEntity<String> recordProgress(@RequestBody progressRequest request){
				 
				 
				 Long courseId = request.getCourseId();
				 
				 Long userId = request.getUserId();
				 
				 Long progress = request.getProgress();
				 
				 
				String message = learningService.recordProgress(userId, courseId, progress);
				 
				return new ResponseEntity<>(message, HttpStatus.CREATED);
				 
			 }
			 
			 @GetMapping("/payment/{amount}")
			    public String Payment(@PathVariable int amount) throws RazorpayException {
			        
			        RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);
			        JSONObject orderRequest = new JSONObject();
			        orderRequest.put("amount", amount);
			        orderRequest.put("currency", "INR");
			        orderRequest.put("receipt", "order_receipt_11");

			        Order order = razorpayClient.orders.create(orderRequest);
			        String orderId = order.get("id");
			        
			        

			        return orderId;
			    }
			 
			 @GetMapping("/coupons")
			  public ResponseEntity<List<Coupon>> listCoupons() {
			      List<Coupon> coupons = couponService.getAllCoupons();
			      return new ResponseEntity<>(coupons, HttpStatus.OK);
			  }

			 
			  @PostMapping("/coupons/add")
			  public ResponseEntity<String> saveCoupon(@RequestBody Coupon coupon) {
			      couponService.saveCoupon(coupon);
			      return new ResponseEntity<>("Added", HttpStatus.CREATED);
			  }

			 

			  @PostMapping("/coupons/edit")
			  public  ResponseEntity<String>  editCoupon(@RequestBody Coupon couponEdit,@RequestParam(value="id") Long id) {
			      Optional<Coupon> coupon = couponService.getCouponById(couponEdit.getId());
			      if (coupon.isPresent()) {
			          Coupon existingCoupon = coupon.get();
			          existingCoupon.setCode(couponEdit.getCode());
			          existingCoupon.setDiscountPercentage(couponEdit.getDiscountPercentage());
			          existingCoupon.setStartDate(couponEdit.getStartDate());
			          existingCoupon.setEndDate(couponEdit.getEndDate());

			          couponService.saveCoupon(existingCoupon);
			          return new ResponseEntity<>("Added", HttpStatus.CREATED);
			      } else {
			    	  return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
			      }
			  }


			  @GetMapping("/coupons/delete")
			  public ResponseEntity<String>  deleteCoupon(@RequestParam(value="id") Long id) {
			      couponService.deleteCoupon(id);
			      return new ResponseEntity<>("Deleted", HttpStatus.OK);
			  }
	

}
