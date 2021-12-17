package com.example.bpower2notifications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/bpower2.jpg")));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Bpower2Notificator");
        stage.setScene(scene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(-5);
        stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 400 );
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}