package com.mansourappdevelopment.maktab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("estshari-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 786, 648);
        stage.setTitle("إستشارى فرع الطرق - إدارة المهندسين العسكريين");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        System.out.println("Developed by Mahmoud Mansour @2021");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
