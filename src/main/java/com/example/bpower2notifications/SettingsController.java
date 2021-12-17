package com.example.bpower2notifications;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SettingsController implements Initializable {
    @FXML
    public  TextField appWidth;
    @FXML
    public  TextField appHeight;
    @FXML
    public  TextField notificateMe; // te nie mogą być static

    Preferences pref;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pref = Preferences.userNodeForPackage(AppController.class);
        appWidth.setText(pref.get("appWidth", ""));
        appHeight.setText(pref.get("appHeight", ""));
        notificateMe.setText(pref.get("notificateMe", ""));
    }

    public void setAppSettings(Event event) throws BackingStoreException {
        Stage stage;
        pref = Preferences.userNodeForPackage(AppController.class);
        if (!appWidth.getText().equals("")){
            pref.put("appWidth",appWidth.getText());
            System.out.println(appWidth.getText());
        }
        if (!appHeight.getText().equals("")){
            pref.put("appHeight",appHeight.getText());
            System.out.println(appHeight.getText());
        }
        if (!notificateMe.getText().equals("")){
            pref.put("notificateMe", notificateMe.getText());
            System.out.println(notificateMe.getText());
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


}

