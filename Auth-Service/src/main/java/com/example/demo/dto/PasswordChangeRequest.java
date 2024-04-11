package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PasswordChangeRequest {
	
	private String userId;
	private String OldPassword;
	private String NewPassword;
	private String ConfirmPassword;
}
