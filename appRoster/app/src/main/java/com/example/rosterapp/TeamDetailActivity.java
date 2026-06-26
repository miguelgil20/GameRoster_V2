package com.example.rosterapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rosterapp.API.API;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import org.json.JSONObject;

public class TeamDetailActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "favoritos_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;

    private Button btnEditTeam;
    private String teamRegion;
    private int teamRanking;

    private TextView txtTeamName, txtRegion, txtRanking;
    private Button btnFavorite, btnPlayers, btnShare;

    private Long teamId;
    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        crearCanalNotificaciones();
        pedirPermisoNotificaciones();

        txtTeamName = findViewById(R.id.txtTeamName);
        txtRegion = findViewById(R.id.txtRegion);
        txtRanking = findViewById(R.id.txtRanking);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnPlayers = findViewById(R.id.btnPlayers);
        btnShare = findViewById(R.id.btnShare);
        btnEditTeam = findViewById(R.id.btnEditTeam);

        teamRegion = getIntent().getStringExtra("TEAM_REGION");
        teamRanking = getIntent().getIntExtra("TEAM_RANKING", 0);
        teamId = getIntent().getLongExtra("TEAM_ID", -1);
        teamName = getIntent().getStringExtra("TEAM_NAME");

        txtTeamName.setText(teamName);
        txtRegion.setText("Región: " + getIntent().getStringExtra("TEAM_REGION"));
        txtRanking.setText("Ranking: " + getIntent().getIntExtra("TEAM_RANKING", 0));

        btnFavorite.setOnClickListener(v -> confirmarFavorito());

        btnPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(this, playerActivity.class);
            intent.putExtra("TEAM_ID", teamId);
            startActivity(intent);
        });

        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Mira este equipo de Valorant: " + teamName);
            startActivity(Intent.createChooser(share, "Compartir equipo"));
        });
        btnEditTeam.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditTeamActivity.class);

            intent.putExtra("TEAM_ID", teamId);
            intent.putExtra("TEAM_NAME", teamName);
            intent.putExtra("TEAM_REGION", teamRegion);
            intent.putExtra("TEAM_RANKING", teamRanking);

            startActivity(intent);
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
                CustomToast.show(
                        TeamDetailActivity.this,
                        "Equipo añadido a favoritos"
                );

                mostrarNotificacionFavorito();
            }

            @Override
            public void onError(UtilREST.Response r) {
                CustomToast.show(
                        TeamDetailActivity.this,
                        "Error al añadir equipos a favoritos"
                );
            }
        });
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Favoritos",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription("Notificaciones de equipos favoritos");

            NotificationManager manager = getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION
                );
            }
        }
    }

    private void mostrarNotificacionFavorito() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(
                        this,
                        "Permiso de notificaciones no concedido",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Equipo añadido a favoritos")
                        .setContentText(teamName + " se ha añadido correctamente")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}