package com.example.rosterapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;



public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private CheckBox checkDarkMode;
    private Spinner spinnerLanguage;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switchNotifications);
        checkDarkMode = findViewById(R.id.checkDarkMode);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Español", "English"}
        );

        spinnerLanguage.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        switchNotifications.setChecked(prefs.getBoolean("notifications", true));
        checkDarkMode.setChecked(prefs.getBoolean("darkMode", false));

        btnSaveSettings.setOnClickListener(v -> {
            prefs.edit()
                    .putBoolean("notifications", switchNotifications.isChecked())
                    .putBoolean("darkMode", checkDarkMode.isChecked())
                    .putString("language", spinnerLanguage.getSelectedItem().toString())
                    .apply();

            Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show();
        });
    }
}