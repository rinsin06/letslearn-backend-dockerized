package com.admin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private Long courseId;

    private String title;
    
    
    @Column(length = 1000) // Increase length to 1000 characters
    private String description;

    private String videoUrl; // Can be null if not a video lesson
    
    @Column(length = 1000) // Increase length to 1000 characters
    private String content; // Textual content

    @Column(name = "lesson_order") // Use a different name for the column
    private Integer lessonOrder; // Position within the course

    // Getters and Setters omitted for brevity
}

