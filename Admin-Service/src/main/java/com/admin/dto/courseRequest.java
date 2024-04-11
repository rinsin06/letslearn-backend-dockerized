package com.admin.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.admin.entity.Category;
import com.admin.entity.Lesson;
import com.admin.entity.Subcategory;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
public class courseRequest {
	

    private Long courseId;

    private String title;

    private String description;
    
    private String authorName;

   
    private String category;
    
    private String imageUrl;
   
    private String subcategory;

    private Double price;

    private String duration;

    private List<Lesson> lessons;

    private Date creationDate;

}
