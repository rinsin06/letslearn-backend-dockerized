package com.admin.dto;

import com.admin.entity.Course;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class lessonRequest {
	
	
	    private Long id;

	
	    private String coursename;

	    private String title;

	    private String description;

	    private String videoUrl; // Can be null if not a video lesson

	    private String content; // Textual content

	    // Use a different name for the column
	    private Integer lessonOrder; // Position within the course


}
