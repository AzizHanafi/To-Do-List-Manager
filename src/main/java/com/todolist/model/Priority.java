package com.todolist.model;

public enum Priority {
    HIGH("Tinggi"),
    MEDIUM("Sedang"),
    LOW("Rendah");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
