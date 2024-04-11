package com.admin.entity;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


    @Entity
    @Data
    @Table(name = "courses")
    public class Course {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long courseId;

        private String title;
        
        @Column(length = 1000) // Increase length to 1000 characters
        private String description;

       
        private String authorName;

        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;
        
        @ManyToOne
        @JoinColumn(name = "subcategory_id")
        private Subcategory subcategory;
        
        @Lob
        @Column(name = "cover_image")
        private String coverImage;

        private Double price;

        private String duration;

        private Date creationDate;

        // Getters and Setters omitted for brevity
    }


    


