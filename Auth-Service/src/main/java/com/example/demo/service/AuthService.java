package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserCredentialDto;
import com.example.demo.entity.UserCredential;
import com.example.demo.repository.UserCredentialRepository;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredentialDto credential) {
    	
    	UserCredential user = new UserCredential();
    	
    	user.setName(credential.getName());
    	user.setEmail(credential.getEmail());
    	
    	user.setActive_courses(0);
    	user.setPassword(passwordEncoder.encode(credential.getPassword()));
    	
    	user.setPhoneNumber(credential.getPassword());
    	 	
    	
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(user);
        return "user added to the system";
    }
    
 public String EditUser(UserCredential credential,int Id) {
	 
	 
	 Optional<UserCredential> existingUser = repository.findById(Id);
	 
	 if(existingUser.isPresent()) {
		 
		 UserCredential user = existingUser.get();
		 
		 user.setEmail(credential.getEmail());
		 user.setName(credential.getName());
		 user.setPhoneNumber(credential.getPhoneNumber());
		 user.setRoles(credential.getRoles());
		 user.setBlocked(credential.isBlocked());
		 
    	 repository.save(user);
		 
		  return "user edited and added to the system";
		 
	 }
    	 	
    	
//        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
//        repository.save(credential);
        return "user doesnt exisit";
    }
    
    

    public String generateToken(String username) {
    	
    	 Optional<UserCredential> existingUser = repository.findByEmail(username);
    	 
    	 UserCredential user = existingUser.get();
    	 
        return jwtService.generateToken(username,user);
    }
    
    public String refreshToken(String username) {
        return jwtService.refreshToken( username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public void updateUserPassword(UserCredential user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }
    
    public List<UserCredential> getAllUsers()
    {
    	return repository.findAll();
    }

}
