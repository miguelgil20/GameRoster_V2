package com.example.rosterapp;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class AboutActivity extends AppCompatActivity {

    private Button btnPlaySound, btnEmail;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnPlaySound = findViewById(R.id.btnPlaySound);
        btnEmail = findViewById(R.id.btnEmail);

        mediaPlayer = MediaPlayer.create(this, R.raw.gameroster);

        btnPlaySound.setOnClickListener(v -> {
            mediaPlayer.start();
            Toast.makeText(this, "Reproduciendo sonido", Toast.LENGTH_SHORT).show();
        });

        btnEmail.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:miguel@example.com"));
            email.putExtra(Intent.EXTRA_SUBJECT, "GameRoster");
            startActivity(email);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}