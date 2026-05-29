package com.example.rosterapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONObject;

public class TeamDetailActivity extends AppCompatActivity {

    private TextView txtTeamName, txtRegion, txtRanking;
    private Button btnFavorite, btnPlayers, btnShare;

    private Long teamId;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        txtTeamName = findViewById(R.id.txtTeamName);
        txtRegion = findViewById(R.id.txtRegion);
        txtRanking = findViewById(R.id.txtRanking);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnPlayers = findViewById(R.id.btnPlayers);
        btnShare = findViewById(R.id.btnShare);

        teamId = getIntent().getLongExtra("TEAM_ID", -1);
        teamName = getIntent().getStringExtra("TEAM_NAME");

        txtTeamName.setText(teamName);
        txtRegion.setText("Región: " + getIntent().getStringExtra("TEAM_REGION"));
        txtRanking.setText("Ranking: " + getIntent().getIntExtra("TEAM_RANKING", 0));

        btnFavorite.setOnClickListener(v -> confirmarFavorito());

        btnPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayersActivity.class);
            intent.putExtra("TEAM_ID", teamId);
            startActivity(intent);
        });

        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Mira este equipo de Valorant: " + teamName);
            startActivity(Intent.createChooser(share, "Compartir equipo"));
        });
    }

    private void confirmarFavorito() {
        new AlertDialog.Builder(this)
                .setTitle("Añadir favorito")
                .setMessage("¿Quieres añadir este equipo a favoritos?")
                .setPositiveButton("Sí", (dialog, which) -> guardarFavorito())
                .setNegativeButton("No", null)
                .show();
    }

    private void guardarFavorito() {
        Long userId = getSharedPreferences("prefs", MODE_PRIVATE)
                .getLong("userId", 1L);

        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        JSONObject json = UtilJSONParser.createFavorite(userId, teamId);

        API.createFavorite(json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                Toast.makeText(TeamDetailActivity.this, "Equipo añadido a favoritos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(TeamDetailActivity.this, "Error al añadir favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }
}