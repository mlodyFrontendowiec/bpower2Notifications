package com.example.bpower2notifications;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.awt.MenuBar;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class NotificationController implements Initializable {
    ObservableList<Object> notifications = FXCollections.observableArrayList();
    NotificationHandler notificationHandler = new NotificationHandler(LoginHandler.token);

    private static String token;
    @FXML
    StackPane pane = new StackPane();
    @FXML
    public MenuItem settings;
    Preferences pref;

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
    ObservableList<String> list = FXCollections.observableArrayList();
    ListView<String> lv = new ListView<>(list);

    static class XCell extends ListCell<String> {
        HBox hbox = new HBox();
        HBox hboxButton = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("Otwórz sprawę w Bpower");
        Button buttonNotificateMeLater = new Button("Przypomnij później");
        Button buttonNotificationFinished = new Button("Zamknij");
        String lastItem;

        public XCell() {
            super();
            hbox.setStyle("-fx-padding: 10px;");
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setStyle("-fx-background-color:#1C4387");
            button.setTextFill(Color.WHITE);
            buttonNotificateMeLater.setStyle("-fx-background-color:#c4a020");
            buttonNotificateMeLater.setTextFill(Color.WHITE);
            buttonNotificationFinished.setStyle("-fx-background-color:#d50d0d");
            buttonNotificationFinished.setTextFill(Color.WHITE);
            hboxButton.getChildren().addAll(buttonNotificateMeLater, buttonNotificationFinished,  button);
            hboxButton.setSpacing(3);
            label.setStyle("-fx-padding: 4px;");
            hbox.getChildren().addAll(label, pane,hboxButton );
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                AppController.getStage().setIconified(false);
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                AppController.getStage().setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 60 -  (getListView().getItems().toArray().length * 50));
                lastItem = item;
                label.setText(item!=null ? item.substring(0,24) : "<null>");
                setGraphic(hbox);
                Preferences pref;
                pref = Preferences.userNodeForPackage(AppController.class);
                String notificateTime =  pref.get("notificateMe", "");

                buttonNotificateMeLater.setOnAction((event)->{
                    setTimeout(()-> {
                        SystemTray tray = SystemTray.getSystemTray();
                        Image image = Toolkit.getDefaultToolkit().createImage("/images/bpower2.jpg");
                        TrayIcon trayIcon = new TrayIcon(image, "Bpower2");
                        trayIcon.setImageAutoSize(true);
                        trayIcon.setToolTip(item.substring(0,24));
                        try {
                            tray.add(trayIcon);
                        } catch (AWTException e) {
                            e.printStackTrace();
                        }
                        trayIcon.displayMessage("Bpower2", item.substring(0,24), TrayIcon.MessageType.INFO);
                    }, Integer.parseInt(notificateTime)  * 1000 * 60);
                });
                Stage stage = AppController.getStage();
                stage.setHeight(60 + getListView().getItems().toArray().length * 60);
                buttonNotificationFinished.setOnAction(event -> {
                    getListView().getItems().remove(getItem());
                    stage.setHeight(60 + getListView().getItems().toArray().length * 60);
                    AppController.getStage().setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 25 -  (getListView().getItems().toArray().length * 50));
                });
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            Desktop d = Desktop.getDesktop();
                            d.browse(new URI("https://b2ng.bpower2.com" + item.substring(25,76)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Timer timer = new Timer();
        timer.schedule(NotificationHandler.getInstance(LoginHandler.token), 0, 1000);
        pref = Preferences.userNodeForPackage(AppController.class);
        if(pref.get("appWidth", "").isBlank()){
            lv.setMinWidth(560);
        }else{
            lv.setMinWidth(Double.parseDouble(pref.get("appWidth", "")));
        }
        if(pref.get("appHeight", "").isBlank()){
            lv.setMinWidth(220);
        }else{
            lv.setMinWidth(Double.parseDouble(pref.get("appHeight", "")));
        }

        settings.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings-view.fxml"));
                stage.getIcons().add(new javafx.scene.image.Image(HelloApplication.class.getResourceAsStream("/images/bpower2.jpg")));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setTitle("Bpower2Notificator");
                stage.setScene(scene);
                stage.show();
            }
        });

        Runnable helloRunnable = () -> {
            if(NotificationHandler.notificationsList.size() > 0){
                Platform.runLater(
                        () -> {
                            JsonObject jsonObject = null;
                            if(NotificationHandler.notificationsList.get(0) != null){
                                jsonObject = NotificationHandler.notificationsList.get(0).getAsJsonObject();
                            }

                            String url = jsonObject.get("variables").getAsJsonObject().get("goToUrl").toString();
                            String title = jsonObject.get("variables").getAsJsonObject().get("title").toString();

                            try{
                                SystemTray tray = SystemTray.getSystemTray();
                                Image image = Toolkit.getDefaultToolkit().createImage("/images/bpower2.jpg");
                                TrayIcon trayIcon = new TrayIcon(image, "Bpower2");
                                trayIcon.setImageAutoSize(true);
                                trayIcon.setToolTip(title);
                                tray.add(trayIcon);
                                trayIcon.displayMessage("Bpower2", title.substring(1,title.length() - 1), TrayIcon.MessageType.INFO);
                            }catch(Exception ex){
                                System.err.print(ex);
                            }
                            list.add(title.substring(1,title.length() - 1).concat(url.substring(0,url.length() - 1)));
                            System.out.println(NotificationHandler.notificationsList);
                            NotificationHandler.notificationsList.clear();
                        }
                );
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 1, TimeUnit.SECONDS);
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return  new XCell();
            }
        });

        pane.setMinWidth(Double.parseDouble(pref.get("appWidth", "")));
        pane.setMinHeight(Double.parseDouble(pref.get("appHeight", "")));

        pane.getChildren().add(lv);
    }


}
