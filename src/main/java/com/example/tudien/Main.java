package com.example.tudien;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        primaryStage.setTitle("Từ điển Anh - Việt");
        primaryStage.setScene(new Scene (root, 800, 500));
        primaryStage.show();
        Image dictionaryLogo = new Image(new File("src/main/java/com/example/tudien/image/dictionary.png").toURI().toString());
        primaryStage.getIcons().add(dictionaryLogo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}