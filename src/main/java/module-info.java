module com.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.todolist             to javafx.fxml;
    opens com.todolist.controller  to javafx.fxml;
    opens com.todolist.model       to com.google.gson;
}
