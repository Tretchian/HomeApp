package com.example.demo;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
   static DBmanager dBmanager;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 429);
        stage.setTitle("Дом контроль");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        dBmanager = new DBmanager(DBmanager.DB_LINK);

        launch();
    }
}