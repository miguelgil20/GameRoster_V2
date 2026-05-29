package com.example.rosterapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;




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
                players.addAll(UtilJSONParser.parseArrayPlayers(r.content));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(playerActivity.this, "Error cargando jugadores", Toast.LENGTH_SHORT).show();
            }
        });
    }
}