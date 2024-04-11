package com.example.demo.feign;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.PasswordChangeRequest;





@FeignClient("AUTH-SERVICE")
public interface AuthInterface {
	
	@GetMapping("/auth/getUser")
    public ResponseEntity<Map<String,String>> getUser(@RequestParam("username") String username);
	
	@PostMapping("/auth/change-password")
    public ResponseEntity<Map<String,String>> changePassword(@RequestBody PasswordChangeRequest request);
}