package com.todolist.model;

public enum Category {
    KULIAH("Kuliah"),
    KERJA("Kerja"),
    PERSONAL("Personal"),
    LAINNYA("Lainnya");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
