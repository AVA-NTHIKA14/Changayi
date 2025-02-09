package com.example.myapplication;
import androidx.annotation.NonNull;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.*;
import android.os.Handler;
import android.os.Looper;


public class GroqAPI {
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String API_KEY ="your_API_Key";

    public interface GroqCallback {
        void onSuccess(String response);

        void onFailure(String error);
    }
        public static void analyzeDiaryEntry(String diaryEntry, GroqCallback callback){
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();

            //json request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.3-70b-versatile");

            //creating array to store, content-system, content-user
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "You are a friendly AI that detects emotions and provides comforting responses."));
            messages.add(Map.of("role", "user", "content", "Diary entry: " + diaryEntry + ". What emotion is expressed, and how should I respond?"));

            requestBody.put("messages", messages);

            //creating java obj to json,authorization,POST,http request
            // Convert Java object to JSON
            String jsonRequest = gson.toJson(requestBody);

            RequestBody body = RequestBody.create(jsonRequest, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            //Exception handling
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onFailure("API request failed: "+e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    String responseData = response.body().string();
                    System.out.println(responseData);
                    if(!response.isSuccessful()){
                        callback.onFailure("API response error: "+ response.message());
                        return;
                    }

                    //resposedata(string)->jsonobject(using jsonparsor of GSON)->extract choices(multiple responses)
                   try {
                       JsonObject jsonResponse = JsonParser.parseString(responseData).getAsJsonObject();
                       JsonArray choices = jsonResponse.getAsJsonArray("choices");

                       //takes 1st response->convert to string->success->failure
                       if(choices != null && choices.size() > 0){
                           JsonObject firstChoice = choices.get(0).getAsJsonObject();
                           JsonObject message = firstChoice.getAsJsonObject("message");
                           String aiResponse = message.get("content").getAsString();
                           callback.onSuccess(aiResponse);
                       }
                       else {
                           callback.onFailure("No response from AI.");
                       }
                   }
                   catch (Exception e) {
                       callback.onFailure("Error parsing AI response: " + e.getMessage());
                   }

                }
            });

    }
}
