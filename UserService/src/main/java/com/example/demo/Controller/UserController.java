package com.example.demo.Controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PasswordChangeRequest;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.WalletInfo;
import com.example.demo.feign.AuthInterface;
import com.example.demo.repository.WalletRepository;
import com.example.demo.service.TwilioOtpService;
import com.example.demo.service.WalletService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;





@RestController
@RequestMapping("/user") 
public class UserController {
	
    @Autowired
	private AuthInterface authInterface;
	
	@Autowired
	private WalletService walletService;
	
	 @Value("${rzp_key_id}")
	    private String keyId;

	    @Value("${rzp_key_secret}")
	    private String secret;

	    @Autowired
		TwilioOtpService otpService;
		
		
	
	@GetMapping("/profile")
	public ResponseEntity<Map<String, Object>> getprofile(@RequestParam("username") String username) {
		
		
		ResponseEntity<Map<String, String>> user = authInterface.getUser(username);
		
		String userId = user.getBody().get("id");
		
		WalletInfo wallet = walletService.getWalletByUserId(Integer.parseInt(userId));
		
		   if(wallet == null) {
	        	
	        	 wallet = new WalletInfo();
	             wallet.setId(Integer.parseInt(userId));
	             
	             Transaction transaction = new Transaction();
	             
	        }
	        
		
		
		Map<String, Object> response = new HashMap<>();
		
		BigDecimal balance;
		
			balance = wallet.getBalance();
			
			response.put("Balance", balance);
		
		
		 List<Transaction> transactions = walletService.getTransactionsForWallet(Integer.parseInt(userId));
		
		
		 
		 
		 response.put("user", user);
		 
		 response.put("transactions", transactions);
		 
		 response.put("Balance", balance);
		 
		 return ResponseEntity.ok(response);
    	 
		
	}
	
	@GetMapping("/wallet")
    public ResponseEntity<Map<String, BigDecimal>>  viewWallet(@RequestParam("id") String Id) {
        
        WalletInfo wallet = walletService.getWalletByUserId(Integer.parseInt(Id));
        
        if(wallet == null) {
        	
        	 wallet = new WalletInfo();
             wallet.setId(Integer.parseInt(Id));
        }
        
        
        Map<String, BigDecimal> response = new HashMap<>();
        
        response.put("Balance", wallet.getBalance());
        
        return ResponseEntity.ok(response);
        
    }
	
	
	@PostMapping("/wallet/add")
	public ResponseEntity<Map<String, String>>  deposit(@RequestBody WalletInfo walletInfo){
		
		int user_id = walletInfo.getId();
		
		BigDecimal amount = walletInfo.getBalance(); 
		
		walletService.deposit(user_id,amount);
		
        Map<String, String> response = new HashMap<>();
        
        response.put("Balance", "added");
		
		
		return ResponseEntity.ok(response);
	}
	
	
	@GetMapping("/wallet/transactions/{id}")
	public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("id") String id){
		
		 List<Transaction> transactions = walletService.getTransactionsForWallet(Integer.parseInt(id));
	        return ResponseEntity.ok(transactions);
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
	  
	  @PostMapping("/change-password")
	  public ResponseEntity<Map<String,String>> changePassword(@RequestBody PasswordChangeRequest request) {
	       
		  ResponseEntity<Map<String,String>> response = authInterface.changePassword(request);
		  
		  return response;
	  }

		
		

		@GetMapping("/sendotp")
		public ResponseEntity<Map<String, String>> sendOtp(@RequestParam("username") String username)
		{
			
			ResponseEntity<Map<String, String>> user = authInterface.getUser(username);
			
			 Map<String, String> response = new HashMap<>();
			
			if(user.getBody() != null) {
				
				String phoneNumber = user.getBody().get("phone");
				
				otpService.sendOtpForVerification("+91"+phoneNumber);
				
				
				  response.put("phone",phoneNumber);
				 response.put("message", "otp sended successfully");
				
				
				return ResponseEntity.ok(response);

			}
			
			
			 response.put("message", "Invalid user name");
			 
			 
			
			

			 return ResponseEntity.ok(response);
		}
		
		@GetMapping("/verify-otp")
		public ResponseEntity<Map<String, String>> verifyotp(@RequestParam(value = "phone") String phone, @RequestParam(value = "otp") String otp) {
		
			
			
		    String msg = otpService.validateOtp(otp,"+91"+phone);
		    
		    Map<String, String> response = new HashMap<>();

		    
		    if (msg.equals("Valid OTP")) { // Use .equals() to compare strings
		    	
		    	
		    	response.put("message", "Verification Successfull");
		    	
		    	return ResponseEntity.ok(response);
		    	

		    } else {
		    	
		    	
		    	response.put("message", "Verification Failed");
		    	
		    	
		    	
		    	return ResponseEntity.ok(response);
		    }
		}
	  
	  
	
//	 @PostMapping("/image-upload")
//	    public ResponseEntity<String> uploadImage(  @RequestPart("image") MultipartFile image) {
//	        try {
//	        	
//	        	
//	        	
//	        	MultipartFile file = image;
//	        	
//	        	if(jwtService.isTokenExpired(token)== true) {
//	        		
//	        		String newToken = jwtService.generateToken(username);
//	        		
//	        		token = newToken;
//	        		
//	        	}
//	            // Decode token and extract user information (you might want to implement this logic)
//	            
//	            
//	            // Find user by username
//	            Optional<UserInfo> optionalUserInfo = repository.findByName(username);
//	            
//	            if (optionalUserInfo.isPresent()) {
//	                UserInfo userInfo = optionalUserInfo.get();
//
//	                // Convert the MultipartFile to byte[] and save it to the user's profileImage field
//	                try {
//						userInfo.setProfileImage(file.getBytes());
//					} catch (java.io.IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//	                // Save the updated user entity to the database
//	                repository.save(userInfo);
//
//	                return ResponseEntity.ok("Image uploaded successfully!");
//	            } else {
//	                return ResponseEntity.status(404).body("User not found");
//	            }
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            return ResponseEntity.status(500).body("Failed to upload image");
//	        }
//	    }
	

}
