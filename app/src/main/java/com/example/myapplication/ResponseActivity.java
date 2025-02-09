package com.example.myapplication;

import static com.example.myapplication.GroqAPI.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ResponseActivity extends AppCompatActivity {
    TextView responseTextView;
    Button regenerateButton;
    Button btnYes;
    Button btnNo;
    String detectedEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_response);


        responseTextView=findViewById(R.id.responseTextView);
        regenerateButton = findViewById(R.id.btnRegen);
        btnYes=findViewById(R.id.yesButton);
        btnNo=findViewById(R.id.noButton);

        String diaryEntry = getIntent().getStringExtra("diaryEntry");

        if (diaryEntry != null && !diaryEntry.isEmpty()) {
            // Call the Groq API to analyze the diary entry
            GroqAPI.analyzeDiaryEntry(diaryEntry, new GroqCallback() {
                @Override
                public void onSuccess(String response) {
                    // Display AI's response in TextView
                    runOnUiThread(() -> responseTextView.setText("AI Response:" + response));
                    detectedEmotion = extractEmotion(response);
                }

                private String extractEmotion(String response) {
                    if (response.toLowerCase().contains("happy")) return "happy";
                    if (response.toLowerCase().contains("sad")) return "sad";
                    if (response.toLowerCase().contains("angry")) return "angry";
                    if (response.toLowerCase().contains("relaxed")) return "relaxed";
                    if (response.toLowerCase().contains("energetic")) return "energetic";
                    return "unknown";
                }
                @Override
                public void onFailure(String error) {
                    // Display error message in TextView
                    runOnUiThread(() -> responseTextView.setText("Error:" + error));
                }
            });
        }

        regenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diaryEntry != null && !diaryEntry.isEmpty()) {
                    // Call the Groq API to analyze the diary entry
                    GroqAPI.analyzeDiaryEntry(diaryEntry, new GroqCallback() {
                        @Override
                        public void onSuccess(String response) {
                            // Display AI's response in TextView
                            runOnUiThread(() -> responseTextView.setText("AI Response:" + response));
                            detectedEmotion = extractEmotion(response);
                        }
                        private String extractEmotion(String response) {
                            if (response.toLowerCase().contains("happy")) return "happy";
                            if (response.toLowerCase().contains("sad")) return "sad";
                            if (response.toLowerCase().contains("angry")) return "angry";
                            if (response.toLowerCase().contains("relaxed")) return "relaxed";
                            if (response.toLowerCase().contains("energetic")) return "energetic";
                            return "unknown";
                        }
                        @Override
                        public void onFailure(String error) {
                            // Display error message in TextView
                            runOnUiThread(() -> responseTextView.setText("Error:" + error));
                        }
                    });
                }
            }
        });


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ResponseActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(ResponseActivity.this,ConnectActivity.class);
                intent2.putExtra("emotion", detectedEmotion);
                startActivity(intent2);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

