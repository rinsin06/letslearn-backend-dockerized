package com.admin.dto;

import java.util.List;

import com.admin.entity.Course;

import lombok.Data;

@Data
public class cartRequest {
	
	private Long userId;
	
	private List<Long> courseId;

}
