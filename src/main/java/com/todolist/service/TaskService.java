package com.todolist.service;

import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.util.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private static TaskService instance;
    private List<Task> tasks;

    private TaskService() {
        tasks = FileManager.loadTasks();
    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
        save();
    }

    public void updateTask(Task updated) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(updated.getId())) {
                tasks.set(i, updated);
                break;
            }
        }
        save();
    }

    public void deleteTask(String id) {
        tasks.removeIf(t -> t.getId().equals(id));
        save();
    }

    public void toggleComplete(String id) {
        tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .ifPresent(t -> {
                    t.setCompleted(!t.isCompleted());
                    save();
                });
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getTasksByCategory(Category category) {
        return tasks.stream()
                .filter(t -> t.getCategory() == category)
                .collect(Collectors.toList());
    }

    public int getTotalCount() {
        return tasks.size();
    }

    public int getCompletedCount() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }

    public double getProgressPercentage() {
        if (tasks.isEmpty()) return 0;
        return (double) getCompletedCount() / getTotalCount() * 100.0;
    }

    private void save() {
        FileManager.saveTasks(tasks);
    }
}
