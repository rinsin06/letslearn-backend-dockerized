package com.admin.dto;

import java.util.List;

import lombok.Data;

@Data
public class lessonWithCourseRequest {
	
	private List<lessonRequest> lessonRequest;
	
	private Long CourseId;
	
	private List<lessonRequest> deleteRequest;

}
