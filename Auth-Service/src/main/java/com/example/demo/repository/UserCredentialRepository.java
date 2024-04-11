package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.UserInfo;
import com.example.demo.entity.UserCredential;

import java.util.Optional;

public interface UserCredentialRepository  extends JpaRepository<UserCredential,Integer> {
	
    Optional<UserCredential> findByName(String username);
    
    Optional<UserCredential> findByEmail(String username);
    
    Optional<UserInfo> findUserInfoByEmail(String username);
}
