package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data

public class UserCredentialDto {
	
	
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    
    private String password;
    private String roles;
    private int active_courses;
    private boolean blocked;

}
