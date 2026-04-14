package com.todolist.controller;

import com.todolist.model.Category;
import com.todolist.model.Priority;
import com.todolist.model.Task;
import com.todolist.service.TaskService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class TaskFormController {

    @FXML private Label dialogTitleLabel;
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private ComboBox<Category> categoryCombo;
    @FXML private ComboBox<Priority> priorityCombo;
    @FXML private DatePicker deadlinePicker;
    @FXML private Label titleError;

    private Task editingTask;
    private Stage stage;
    private final TaskService taskService = TaskService.getInstance();

    @FXML
    public void initialize() {
        // ── Category ComboBox ──
        categoryCombo.getItems().addAll(Category.values());
        categoryCombo.setValue(Category.LAINNYA);
        categoryCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Category c) { return c != null ? c.getDisplayName() : ""; }
            @Override public Category fromString(String s) { return null; }
        });

        // ── Priority ComboBox ──
        priorityCombo.getItems().addAll(Priority.values());
        priorityCombo.setValue(Priority.MEDIUM);
        priorityCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Priority p) { return p != null ? p.getDisplayName() : ""; }
            @Override public Priority fromString(String s) { return null; }
        });

        // ── Disable past dates in DatePicker ──
        deadlinePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #F3F4F6;");
                }
            }
        });

        // Sync managed with visible so hidden label takes no space
        titleError.managedProperty().bind(titleError.visibleProperty());

        // Clear title error on type
        titleField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                titleField.getStyleClass().remove("input-error");
                titleError.setVisible(false);
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTask(Task task) {
        this.editingTask = task;
        dialogTitleLabel.setText("Edit Task");
        titleField.setText(task.getTitle());
        descField.setText(task.getDescription() != null ? task.getDescription() : "");
        categoryCombo.setValue(task.getCategory());
        priorityCombo.setValue(task.getPriority());
        deadlinePicker.setValue(task.getDeadline());
    }

    @FXML
    private void onSave() {
        String title = titleField.getText().trim();

        if (title.isBlank()) {
            titleField.getStyleClass().add("input-error");
            titleError.setVisible(true);
            titleField.requestFocus();
            return;
        }

        Category category = categoryCombo.getValue() != null ? categoryCombo.getValue() : Category.LAINNYA;
        Priority priority = priorityCombo.getValue() != null ? priorityCombo.getValue() : Priority.MEDIUM;
        String desc       = descField.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();

        if (editingTask == null) {
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(desc);
            newTask.setCategory(category);
            newTask.setPriority(priority);
            newTask.setDeadline(deadline);
            taskService.addTask(newTask);
        } else {
            editingTask.setTitle(title);
            editingTask.setDescription(desc);
            editingTask.setCategory(category);
            editingTask.setPriority(priority);
            editingTask.setDeadline(deadline);
            taskService.updateTask(editingTask);
        }

        stage.close();
    }

    @FXML
    private void onCancel() {
        stage.close();
    }
}
