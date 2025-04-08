package com.example.TodoList.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String owner;  // Stores username of the todo owner
    private LocalDate dueDate;  // Date when task is due (YYYY-MM-DD)

    // Constructor for JPA
    public Todo() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor for creating new todos
    public Todo(String title, String description, LocalDate dueDate) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }
}