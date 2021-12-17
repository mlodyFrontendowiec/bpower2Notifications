package com.example.bpower2notifications;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.TimerTask;


public class NotificationHandler extends TimerTask {
    public String url = "https://b2ng.bpower2.com/index.php/restApi/user/method/notifications";
    private String token;
    public static ArrayList<JsonElement> notificationsList = new ArrayList<JsonElement>();
    public JsonElement notifications = new JsonElement() {
        @Override
        public JsonElement deepCopy() {
            return null;
        }
    };
    private static NotificationHandler INSTANCE;

    public NotificationHandler(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static NotificationHandler getInstance(String token) {
        if(INSTANCE == null) {
            INSTANCE = new NotificationHandler(token);
        }

        return INSTANCE;
    }


    public void  run(){
        JsonElement convertedResponse = getNotifications(token);
        if (convertedResponse.getAsJsonArray().isEmpty()) {
            return;
        }
        notificationsList.add(convertedResponse.getAsJsonArray().get(0));

    }

    public void setNotifications(JsonElement notifications) {
        this.notifications = notifications;
    }

    public JsonElement getNotification() {
        return notifications;
    }


    public JsonElement getNotifications(String token) {
        setToken(token);
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .setHeader("Authorization",LoginHandler.token.substring(1,LoginHandler.token.length() - 1))
                    .build();
            HttpResponse<String> response = null;
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject convertedNotifications = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println(convertedNotifications);
            if (response.statusCode() == 200 ){
                setNotifications(convertedNotifications.get("notifications"));
            }
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }

        return getNotification();
    }

}
