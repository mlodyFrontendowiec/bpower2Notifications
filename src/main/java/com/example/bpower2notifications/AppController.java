package com.example.bpower2notifications;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AppController  {
    Preferences pref;
    public static Stage stage;
    public static Scene scene;
    private static String token;
    @FXML
    private TextField loginInput, passwordInput;
    @FXML
    private Label comunicate;
    @FXML
    private void handleButtonAction (ActionEvent event) throws IOException {
        String login = loginInput.getText();
        String password = passwordInput.getText();
        LoginHandler loginHandler = new LoginHandler();
        loginHandler.handleLogin(login,password);

        if (login.isBlank() || password.isBlank()){
            comunicate.setText("Please enter your login and password.");
            comunicate.setTextFill((Color.color(1, 0, 0)));
        }
        else if (loginHandler.handleLogin(login,password).equals("Error")){
            comunicate.setText("Incorrect username or password.");
            comunicate.setTextFill((Color.color(1, 0, 0)));
        }else{
            setToken(LoginHandler.token);
            handleChangeScene(event);
            stage.setIconified(true);
        }
    }


    public void handleChangeScene (ActionEvent event) throws IOException {
        pref = Preferences.userNodeForPackage(AppController.class);
        Parent panel = FXMLLoader.load(getClass().getResource("panel-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        if(pref.get("appWidth", "").isBlank()){
            scene = new Scene(panel,460, 60);
        }else{
            scene = new Scene(panel,Double.parseDouble(pref.get("appWidth", "")), Double.parseDouble(pref.get("appHeight", "")));
        }
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        if(pref.get("appWidth", "").isBlank()){
            stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 25);
        }else{
            stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - Double.parseDouble(pref.get("appHeight", "")) );
        }
        stage.setScene(scene);
        stage.hide();
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
