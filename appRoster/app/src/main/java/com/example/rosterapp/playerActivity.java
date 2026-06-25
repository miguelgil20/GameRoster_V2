package com.example.rosterapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.rosterapp.API.API;
import com.example.rosterapp.API.PlayerAdapter;
import com.example.rosterapp.API.PlayerModel;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    private ListView listPlayers;
    private ArrayList<PlayerModel> players;
    private PlayerAdapter adapter;
    private Long teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        listPlayers = findViewById(R.id.listPlayers);
        teamId = getIntent().getLongExtra("TEAM_ID", -1);

        players = new ArrayList<>();
        adapter = new PlayerAdapter(this, players);
        listPlayers.setAdapter(adapter);

        cargarJugadores();
    }

    private void cargarJugadores() {
        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        API.getPlayers(token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                players.clear();

                ArrayList<PlayerModel> todosLosJugadores =
                        UtilJSONParser.parseArrayPlayers(r.content);

                for (PlayerModel player : todosLosJugadores) {
                    if (player.getTeamId().equals(teamId)) {
                        players.add(player);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(UtilREST.Response r) {
                CustomToast.show(
                        playerActivity.this,
                        "Error cargando jugadores"
                );
            }
        });
    }
}