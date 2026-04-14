package com.todolist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/todolist/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 960, 720);
        stage.setTitle("TaskFlow — To-Do List Manager");
        stage.setScene(scene);
        stage.setMinWidth(720);
        stage.setMinHeight(560);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
