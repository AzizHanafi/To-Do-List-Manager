package com.todolist.controller;

import com.todolist.model.Category;
import com.todolist.model.Priority;
import com.todolist.model.Task;
import com.todolist.service.TaskService;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    // ── Sidebar nav items ──
    @FXML private VBox   sidebar;
    @FXML private HBox   navAll;
    @FXML private VBox   cardKuliah, cardKerja, cardPersonal, cardLainnya;
    @FXML private VBox   chipHigh, chipMedium, chipLow;

    // ── Sidebar count badges ──
    @FXML private Label  countAll, countKuliah, countKerja, countPersonal, countLainnya;
    @FXML private Label  countHigh, countMedium, countLow;

    // ── Top bar ──
    @FXML private Button addBtn;
    @FXML private TextField searchField;
    @FXML private Label  activeFilterLabel;

    // ── Progress card ──
    @FXML private ProgressBar progressBar;
    @FXML private Label progressCountLabel, progressPercentLabel, progressDescLabel;

    // ── Task list ──
    @FXML private VBox taskListContainer;

    // ── State ──
    private TaskService taskService;
    private Category    currentCategory = null;
    private Priority    currentPriority = null;
    private VBox        activeNavCategoryItem = null;
    private VBox        activeNavPriorityItem = null;
    private String      searchQuery = "";

    private boolean sidebarOpen = true;
    private static final double SIDEBAR_WIDTH = 250;

    // ─────────────────────────────────────────────
    //  Init
    // ─────────────────────────────────────────────

    @FXML
    public void initialize() {
        taskService = TaskService.getInstance();

        // Clip sidebar so content doesn't bleed during animation
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(sidebar.widthProperty());
        clip.heightProperty().bind(sidebar.heightProperty());
        sidebar.setClip(clip);

        refreshTaskList();
        updateProgress();
        updateNavCounts();
    }

    // ─────────────────────────────────────────────
    //  Sidebar toggle (hamburger)
    // ─────────────────────────────────────────────

    @FXML
    private void toggleSidebar() {
        double target = sidebarOpen ? 0 : SIDEBAR_WIDTH;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(270),
                        new KeyValue(sidebar.prefWidthProperty(), target, Interpolator.EASE_BOTH),
                        new KeyValue(sidebar.minWidthProperty(), target, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();
        sidebarOpen = !sidebarOpen;
    }

    // ─────────────────────────────────────────────
    //  Navigation handlers — Semua
    // ─────────────────────────────────────────────

    @FXML private void onNavAll() {
        currentCategory = null;
        currentPriority = null;
        clearActiveNavCategory();
        clearActiveNavPriority();
        navAll.getStyleClass().add("nav-item-active");
        syncFilterLabel();
        refreshTaskList();
    }

    // ─────────────────────────────────────────────
    //  Navigation handlers — Kategori
    // ─────────────────────────────────────────────

    @FXML private void onNavKuliah()   { toggleCategoryFilter(Category.KULIAH,    cardKuliah);   }
    @FXML private void onNavKerja()    { toggleCategoryFilter(Category.KERJA,     cardKerja);    }
    @FXML private void onNavPersonal() { toggleCategoryFilter(Category.PERSONAL,  cardPersonal); }
    @FXML private void onNavLainnya()  { toggleCategoryFilter(Category.LAINNYA,   cardLainnya);  }

    // ─────────────────────────────────────────────
    //  Navigation handlers — Prioritas
    // ─────────────────────────────────────────────

    @FXML private void onNavHigh()   { togglePriorityFilter(Priority.HIGH,   chipHigh);   }
    @FXML private void onNavMedium() { togglePriorityFilter(Priority.MEDIUM, chipMedium); }
    @FXML private void onNavLow()    { togglePriorityFilter(Priority.LOW,    chipLow);    }

    // ─────────────────────────────────────────────
    //  Search
    // ─────────────────────────────────────────────

    @FXML private void onSearch() {
        searchQuery = searchField.getText().trim().toLowerCase();
        refreshTaskList();
    }

    // ─────────────────────────────────────────────
    //  Open add dialog
    // ─────────────────────────────────────────────

    @FXML private void openAddDialog() {
        openTaskDialog(null);
    }

    // ─────────────────────────────────────────────
    //  Filter helpers
    // ─────────────────────────────────────────────

    private void toggleCategoryFilter(Category cat, VBox navItem) {
        if (currentCategory == cat) {
            // Deselect → back to "Semua" (only if no priority active either)
            currentCategory = null;
            clearActiveNavCategory();
        } else {
            currentCategory = cat;
            setActiveNavCategory(navItem);
        }
        updateNavAllHighlight();
        syncFilterLabel();
        refreshTaskList();
    }

    private void togglePriorityFilter(Priority prio, VBox navItem) {
        if (currentPriority == prio) {
            currentPriority = null;
            clearActiveNavPriority();
        } else {
            currentPriority = prio;
            setActiveNavPriority(navItem);
        }
        updateNavAllHighlight();
        syncFilterLabel();
        refreshTaskList();
    }

    private void setActiveNavCategory(VBox item) {
        clearActiveNavCategory();
        activeNavCategoryItem = item;
        item.getStyleClass().add("cat-card-active");
    }

    private void clearActiveNavCategory() {
        if (activeNavCategoryItem != null) {
            activeNavCategoryItem.getStyleClass().remove("cat-card-active");
            activeNavCategoryItem = null;
        }
    }

    private void setActiveNavPriority(VBox item) {
        clearActiveNavPriority();
        activeNavPriorityItem = item;
        item.getStyleClass().add("prio-chip-active");
    }

    private void clearActiveNavPriority() {
        if (activeNavPriorityItem != null) {
            activeNavPriorityItem.getStyleClass().remove("prio-chip-active");
            activeNavPriorityItem = null;
        }
    }

    private void updateNavAllHighlight() {
        if (currentCategory == null && currentPriority == null) {
            if (!navAll.getStyleClass().contains("nav-item-active"))
                navAll.getStyleClass().add("nav-item-active");
        } else {
            navAll.getStyleClass().remove("nav-item-active");
        }
    }

    private void syncFilterLabel() {
        StringBuilder sb = new StringBuilder();
        if (currentCategory != null) sb.append(currentCategory.getDisplayName());
        if (currentCategory != null && currentPriority != null) sb.append("  ·  ");
        if (currentPriority != null) sb.append(currentPriority.getDisplayName());
        activeFilterLabel.setText(sb.toString());
    }

    // ─────────────────────────────────────────────
    //  Task list builder
    // ─────────────────────────────────────────────

    private void refreshTaskList() {
        taskListContainer.getChildren().clear();

        List<Task> tasks = taskService.getAllTasks();

        if (currentCategory != null)
            tasks = tasks.stream().filter(t -> t.getCategory() == currentCategory).collect(Collectors.toList());

        if (currentPriority != null)
            tasks = tasks.stream().filter(t -> t.getPriority() == currentPriority).collect(Collectors.toList());

        if (!searchQuery.isEmpty())
            tasks = tasks.stream()
                    .filter(t -> t.getTitle().toLowerCase().contains(searchQuery)
                            || (t.getDescription() != null
                                && t.getDescription().toLowerCase().contains(searchQuery)))
                    .collect(Collectors.toList());

        if (tasks.isEmpty()) {
            taskListContainer.getChildren().add(buildEmptyState());
            return;
        }

        List<Task> incomplete = tasks.stream().filter(t -> !t.isCompleted()).collect(Collectors.toList());
        List<Task> completed  = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());

        // Sort incomplete by priority order
        incomplete.sort((a, b) -> a.getPriority().compareTo(b.getPriority()));

        if (!incomplete.isEmpty()) {
            taskListContainer.getChildren().add(buildSectionLabel("BELUM SELESAI  (" + incomplete.size() + ")", false));
            for (Task t : incomplete) taskListContainer.getChildren().add(buildTaskCard(t));
        }

        if (!completed.isEmpty()) {
            Label header = buildSectionLabel("SELESAI  (" + completed.size() + ")", true);
            VBox.setMargin(header, new Insets(18, 0, 0, 0));
            taskListContainer.getChildren().add(header);
            for (Task t : completed) taskListContainer.getChildren().add(buildTaskCard(t));
        }
    }

    // ─────────────────────────────────────────────
    //  Task Card
    // ─────────────────────────────────────────────

    private Node buildTaskCard(Task task) {
        HBox card = new HBox(14);
        card.getStyleClass().add("task-card");
        if (task.isCompleted()) card.getStyleClass().add("task-card-completed");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(Double.MAX_VALUE);

        // Checkbox
        CheckBox cb = new CheckBox();
        cb.setSelected(task.isCompleted());
        cb.getStyleClass().add("task-checkbox");
        cb.setOnAction(e -> {
            taskService.toggleComplete(task.getId());
            refreshTaskList();
            updateProgress();
            updateNavCounts();
        });

        // Info block
        VBox info = new VBox(5);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

        Label titleLbl = new Label(task.getTitle());
        titleLbl.getStyleClass().add(task.isCompleted() ? "task-title-done" : "task-title");
        titleLbl.setWrapText(true);
        info.getChildren().add(titleLbl);

        if (task.getDescription() != null && !task.getDescription().isBlank()) {
            Label descLbl = new Label(task.getDescription());
            descLbl.getStyleClass().add("task-desc");
            descLbl.setWrapText(true);
            info.getChildren().add(descLbl);
        }

        // Meta row
        HBox meta = new HBox(8);
        meta.setAlignment(Pos.CENTER_LEFT);
        meta.setPadding(new Insets(4, 0, 0, 0));

        Label catBadge = new Label(task.getCategory().getDisplayName());
        catBadge.getStyleClass().addAll("badge", "badge-cat-" + task.getCategory().name().toLowerCase());

        Label prioBadge = new Label("● " + task.getPriority().getDisplayName());
        prioBadge.getStyleClass().addAll("badge", "badge-prio-" + task.getPriority().name().toLowerCase());

        meta.getChildren().addAll(catBadge, prioBadge);

        if (task.getDeadline() != null) {
            boolean overdue = !task.isCompleted() && task.getDeadline().isBefore(LocalDate.now());
            Label dl = new Label("📅 " + task.getDeadline().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            dl.getStyleClass().add(overdue ? "deadline-overdue" : "deadline-label");
            meta.getChildren().add(dl);
        }

        info.getChildren().add(meta);

        // Action buttons
        HBox actions = new HBox(4);
        actions.setAlignment(Pos.CENTER);

        Button editBtn = new Button("✏");
        editBtn.getStyleClass().add("btn-icon-action");
        editBtn.setTooltip(new Tooltip("Edit Task"));
        editBtn.setOnAction(e -> openTaskDialog(task));

        Button delBtn = new Button("🗑");
        delBtn.getStyleClass().addAll("btn-icon-action", "btn-icon-danger");
        delBtn.setTooltip(new Tooltip("Hapus Task"));
        delBtn.setOnAction(e -> confirmDelete(task));

        actions.getChildren().addAll(editBtn, delBtn);
        card.getChildren().addAll(cb, info, actions);
        return card;
    }

    // ─────────────────────────────────────────────
    //  Empty State
    // ─────────────────────────────────────────────

    private Node buildEmptyState() {
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(70, 0, 70, 0));

        Label icon  = new Label(searchQuery.isEmpty() ? "📋" : "🔍");
        icon.getStyleClass().add("empty-icon");

        Label title = new Label(searchQuery.isEmpty() ? "Tidak ada task di sini" : "Task tidak ditemukan");
        title.getStyleClass().add("empty-title");

        Label desc  = new Label(searchQuery.isEmpty()
                ? "Klik \"＋ Tambah Task\" untuk mulai mencatat"
                : "Coba gunakan kata kunci yang berbeda");
        desc.getStyleClass().add("empty-desc");
        desc.setWrapText(true);
        desc.setMaxWidth(300);

        box.getChildren().addAll(icon, title, desc);
        return box;
    }

    // ─────────────────────────────────────────────
    //  Progress & Counts
    // ─────────────────────────────────────────────

    private void updateProgress() {
        int total = taskService.getTotalCount();
        int done  = taskService.getCompletedCount();
        double pct = taskService.getProgressPercentage();

        progressCountLabel.setText(done + " / " + total + " task selesai");
        progressPercentLabel.setText((int) pct + "%");
        progressBar.setProgress(total == 0 ? 0 : pct / 100.0);

        if (total == 0)       progressDescLabel.setText("Mulai dengan menambahkan task pertamamu!");
        else if (pct >= 100)  progressDescLabel.setText("🎉 Luar biasa! Semua task sudah selesai!");
        else                  progressDescLabel.setText("Semangat! Masih ada " + (total - done) + " task yang menunggu");
    }

    private void updateNavCounts() {
        List<Task> all = taskService.getAllTasks();
        countAll.setText(String.valueOf(all.size()));
        countKuliah.setText(String.valueOf(all.stream().filter(t -> t.getCategory() == Category.KULIAH).count()));
        countKerja.setText(String.valueOf(all.stream().filter(t -> t.getCategory() == Category.KERJA).count()));
        countPersonal.setText(String.valueOf(all.stream().filter(t -> t.getCategory() == Category.PERSONAL).count()));
        countLainnya.setText(String.valueOf(all.stream().filter(t -> t.getCategory() == Category.LAINNYA).count()));
        countHigh.setText(String.valueOf(all.stream().filter(t -> t.getPriority() == Priority.HIGH).count()));
        countMedium.setText(String.valueOf(all.stream().filter(t -> t.getPriority() == Priority.MEDIUM).count()));
        countLow.setText(String.valueOf(all.stream().filter(t -> t.getPriority() == Priority.LOW).count()));
    }

    // ─────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────

    private Label buildSectionLabel(String text, boolean muted) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(muted ? "section-label-muted" : "section-label");
        return lbl;
    }

    private void confirmDelete(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Task");
        alert.setHeaderText(null);
        alert.setContentText("Hapus task \"" + task.getTitle() + "\"?\nTask yang dihapus tidak bisa dikembalikan.");
        alert.initOwner(addBtn.getScene().getWindow());
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                taskService.deleteTask(task.getId());
                refreshTaskList();
                updateProgress();
                updateNavCounts();
            }
        });
    }

    private void openTaskDialog(Task taskToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainController.class.getResource("/com/todolist/fxml/task_form.fxml"));
            VBox root = loader.load();
            TaskFormController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(addBtn.getScene().getWindow());
            dialog.setResizable(false);
            dialog.setTitle(taskToEdit == null ? "Tambah Task Baru" : "Edit Task");

            dialog.setScene(new Scene(root));
            ctrl.setStage(dialog);
            if (taskToEdit != null) ctrl.setTask(taskToEdit);

            dialog.showAndWait();

            refreshTaskList();
            updateProgress();
            updateNavCounts();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
