package com.todolist.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private LocalDate deadline;
    private boolean completed;
    private LocalDateTime createdAt;

    public Task() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.completed = false;
        this.priority = Priority.MEDIUM;
        this.category = Category.LAINNYA;
    }

    // ===== Getters =====

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDeadline() { return deadline; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ===== Setters =====

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
