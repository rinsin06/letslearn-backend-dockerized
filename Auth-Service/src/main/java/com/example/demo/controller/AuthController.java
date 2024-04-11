package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.PasswordChangeRequest;
import com.example.demo.dto.ResetRequest;
import com.example.demo.dto.UserCredentialDto;
import com.example.demo.entity.UserCredential;
import com.example.demo.repository.UserCredentialRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;
    
    @Autowired
    private JwtService tokenService;
    
    @Autowired
    private UserCredentialRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    public static final String AUTH_SERVICE = "auth-service";
    
    
    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredentialDto user) {
    	
        return service.saveUser(user);
    }
    
    @PostMapping("/editUser")
    public String editUser(@RequestParam("id") String Id, @RequestBody UserCredential user) {
    	
    	
    	
        return service.EditUser(user,Integer.parseInt(Id));
    }
    
    
    
    @GetMapping("/getUser")
    @CircuitBreaker(name = AUTH_SERVICE,fallbackMethod = "returnNullUser" )
    public ResponseEntity<Map<String,String>> getUser(@RequestParam("username") String username){
    	
    	Optional<UserCredential> user = repository.findByEmail(username);
    	
    	 Map<String, String> response = new HashMap<>();
    	 
    	 
    	 response.put("name", user.get().getName());
    	 response.put("email", user.get().getEmail());
    	 response.put("phone", user.get().getPhoneNumber());
    	 response.put("role", user.get().getRoles());
    	 response.put( "id", String.valueOf(user.get().getId()));
    	 
    	 return ResponseEntity.ok(response);
 	 
    	 
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody ResetRequest request){
    	
    	
    	Optional<UserCredential> user = repository.findByEmail(request.getEmail());
    	
    	
    	UserCredential loggedUser = user.get();
    	
    	service.updateUserPassword(loggedUser, request.getNewPassword());
    	
    	Map<String,String> response = new HashMap<>();
    	
    	 response.put("message", "Password updated successfully");
    	 
    	 
    	 return ResponseEntity.ok(response);
    	
    	
    	
    }
    
    
    
    
    
    
    
    @PostMapping("/change-password")
    @CircuitBreaker(name = AUTH_SERVICE,fallbackMethod = "returnErrorMessage" )
    public ResponseEntity<Map<String,String>> changePassword(@RequestBody PasswordChangeRequest request) {
    	Optional<UserCredential> user = repository.findById(Integer.parseInt(request.getUserId()));
    	
    	Map<String,String> response = new HashMap<>();
    	
    	UserCredential loggedInUser = user.get();
    	
    	String currentPassword = request.getOldPassword();
    	
    	String newPassword = request.getNewPassword();
    	
        if (!passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            // Current password is incorrect, display an error message
            response.put("message", "Invalid Credential");
            
            return ResponseEntity.ok(response);
        } 
        else {
            // Update the user's password
        	
        	 service.updateUserPassword(loggedInUser, newPassword);
        	 
        	 response.put("message", "Password updated successfully");
        	 
        	 
        	 return ResponseEntity.ok(response);
           
            
        }

      
    }
    
    public ResponseEntity<Map<String,String>> returnErrorMessage(Exception e){
    	
    	Map<String, String> response = new HashMap<>();
    	
    	response.put("message", "Please try again after sometime");
    	
    	
    	return ResponseEntity.ok(response);
    	
    }
    

    
    public ResponseEntity<Map<String,String>> returnNullUser(Exception e)
    {
    	
    	Map<String, String> response = new HashMap<>();
    	
    	response.put("name", "null");
    	response.put("email", "null");
    	response.put("phone", "null");
    	response.put("role", "null");
    	
    	 return ResponseEntity.ok(response);
    	
    }

    
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(@RequestBody AuthRequest authRequest) {
    	Map<String, String> response = new HashMap<>();
        Authentication authenticate;
		try {
			authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (AuthenticationException e) {
			
			e.printStackTrace();
			response.put("message", "Invalid User");
        	response.put("status","false");
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			// TODO Auto-generated catch block
			
		}
        if (authenticate.isAuthenticated()) {
            String token = service.generateToken(authRequest.getUsername());
            String refreshToken = service.refreshToken(authRequest.getUsername());
             Optional<UserCredential> user = repository.findByEmail(authRequest.getUsername());
             
             if(user.get().isBlocked() == true)
             {
            	 
            	 response.put("message", "Invalid User");
             	response.put("status","false");
             	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            	 
             }
             
             int userId = user.get().getId();
             response.put("refreshToken", refreshToken);
             response.put("token", token);
             response.put("name", user.get().getName());
             response.put("email", authRequest.getUsername()); // You may replace this with the actual email
             response.put("role",user.get().getRoles());
             response.put("id",String.valueOf(userId) );
             

             return ResponseEntity.ok(response);
        } else {
        	
        	response.put("message", "Invalid User");
        	response.put("status","false");
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            
        }
    }
    
    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestBody String refreshToken)
    {
    	
    	Map<String, String> response = new HashMap<>();
    	try {
			tokenService.validateToken(refreshToken);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.put("message", "Invalid User");
        	response.put("status","false");
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
    	
    	String userName = tokenService.extractUserName(refreshToken);
    	
    	
    	
    	String accessToken = service.generateToken(userName);
    	
    	response.put("accessToken", accessToken);
    	
    	 return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @CircuitBreaker(name = AUTH_SERVICE,fallbackMethod = "returnInvalidToken" )
    public ResponseEntity<Map<String, String>> validateToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Map<String, String> response = new HashMap<>();

        String username = "";
        String refreshToken = "";

        
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                            // Validate the token
                            service.validateToken(token);

                            // Token is valid, proceed with refreshing
                           

                            response.put("token", "valid");}
                    
                    return ResponseEntity.ok(response);
                }
            }
            return ResponseEntity.ok(response);
}
    
    
    
    public ResponseEntity<Map<String,String>> returnInvalidToken(Exception e ){
    	
    	
    	 Map<String, String> response = new HashMap<>();
    	 
    	 response.put("token", "Service is down");
    	 
    	 return ResponseEntity.ok(response);

    	
    }
    
    @GetMapping("/all-users")
    public ResponseEntity<List<UserCredential>> getAllUsers(){
    	
    	
    	List<UserCredential> users = service.getAllUsers();
    	
    	
    	return ResponseEntity.ok(users);
    	
    	
    }
    
    
   
   
}




