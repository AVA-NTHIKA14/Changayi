package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

public class ConnectActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    boolean isPlaying = false;
    Button btnBack;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connect);

        btnBack=findViewById(R.id.btnBack);
        btnHome=findViewById(R.id.btnHome);
        Button playMusicButton = findViewById(R.id.playMusicButton);
        String emotion = getIntent().getStringExtra("emotion");
        // Select music based on emotion
        int musicResId = getMusicForEmotion(emotion);
        if (musicResId != 0) {
            mediaPlayer = MediaPlayer.create(this, musicResId);
        }

        playMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (isPlaying) {
                        mediaPlayer.pause();
                        playMusicButton.setText("🎵 Play Music");
                    } else {
                        mediaPlayer.start();
                        playMusicButton.setText("⏸ Pause Music");
                    }
                    isPlaying = !isPlaying;
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ConnectActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ConnectActivity.this,ResponseActivity.class);
                startActivity(intent2);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int getMusicForEmotion(String emotion) {
        if (emotion == null) return 0;

        switch (emotion.toLowerCase()) {
            case "happy":
                return R.raw.happy;
            case "sad":
                return R.raw.sad;
            case "angry":
                return R.raw.angry;
            case "relaxed":
                return R.raw.relax;
            case "energetic":
                return R.raw.energetic;
            default:
                return 0; // No music if emotion is unknown
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}