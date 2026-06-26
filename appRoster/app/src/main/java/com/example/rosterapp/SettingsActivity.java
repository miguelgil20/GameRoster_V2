package com.example.rosterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import android.content.res.Configuration;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;



public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private CheckBox checkDarkMode;
    private Spinner spinnerLanguage;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        String language = prefs.getString("language", "es");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
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



        switchNotifications.setChecked(prefs.getBoolean("notifications", true));
        checkDarkMode.setChecked(prefs.getBoolean("darkMode", false));

        btnSaveSettings.setOnClickListener(v -> {
            String languageCode =
                    spinnerLanguage.getSelectedItemPosition() == 0 ? "es" : "en";

            prefs.edit()
                    .putBoolean("notifications", switchNotifications.isChecked())
                    .putBoolean("darkMode", checkDarkMode.isChecked())
                    .putString("language", languageCode)
                    .apply();

            CustomToast.show(
                    this,
                    "Preferencias Guardadas"
            );
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}