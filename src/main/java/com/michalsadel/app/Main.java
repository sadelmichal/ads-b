package com.michalsadel.app;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }
}
