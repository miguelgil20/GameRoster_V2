package com.example.rosterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.res.Configuration;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private Button btnTeams, btnFavorites, btnSettings, btnAbout;

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
        setContentView(R.layout.activity_main);

        btnTeams = findViewById(R.id.btnTeams);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnSettings = findViewById(R.id.btnSettings);
        btnAbout = findViewById(R.id.btnAbout);

        btnTeams.setOnClickListener(v ->
                startActivity(new Intent(this, TeamActivity.class)));

        btnFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {
            getSharedPreferences("prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}