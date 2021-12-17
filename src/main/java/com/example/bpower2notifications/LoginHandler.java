package com.example.bpower2notifications;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LoginHandler {
    public static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        LoginHandler.token = token;
    }

    public String handleLogin (String login,String password){
        UserKey userKey = new UserKey(login,password);
        String url = "https://b2ng.bpower2.com/index.php/restApi/generateJWT";

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        String json = new StringBuilder()
                .append("{")
                .append("\"user-key\":\"" + userKey.getUserKey() + "\"")
                .append("}").toString();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .uri(URI.create(url))
                    .setHeader("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = null;
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 && !response.body().toString().substring(2,17).equals("Bad credentials")){
                JsonObject convertedObject = new Gson().fromJson(response.body().toString(), JsonObject.class);
                setToken(convertedObject.get("token").toString());
            }else
            {
                return "Error";
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return getToken();
    }

}
