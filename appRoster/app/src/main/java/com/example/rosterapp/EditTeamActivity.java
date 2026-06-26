package com.example.rosterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rosterapp.API.API;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;


import org.json.JSONObject;

public class EditTeamActivity extends AppCompatActivity {

    private EditText etTeamName;
    private EditText etTeamRegion;
    private EditText etTeamRanking;
    private Button btnSaveTeam;

    private Long teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        etTeamName = findViewById(R.id.etTeamName);
        etTeamRegion = findViewById(R.id.etTeamRegion);
        etTeamRanking = findViewById(R.id.etTeamRanking);
        btnSaveTeam = findViewById(R.id.btnSaveTeam);

        teamId = getIntent().getLongExtra("TEAM_ID", -1);

        etTeamName.setText(getIntent().getStringExtra("TEAM_NAME"));
        etTeamRegion.setText(getIntent().getStringExtra("TEAM_REGION"));
        etTeamRanking.setText(
                String.valueOf(getIntent().getIntExtra("TEAM_RANKING", 0))
        );

        btnSaveTeam.setOnClickListener(v -> guardarCambios());
    }

    private void guardarCambios() {
        String name = etTeamName.getText().toString().trim();
        String region = etTeamRegion.getText().toString().trim();
        String rankingText = etTeamRanking.getText().toString().trim();

        if (name.isEmpty() || region.isEmpty() || rankingText.isEmpty()) {
            CustomToast.show(this,"Rellena todos los campos");
            return;
        }

        int ranking = Integer.parseInt(rankingText);

        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        JSONObject json = UtilJSONParser.createTeam(name, region, ranking);

        API.updateTeam(teamId, json, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                CustomToast.show(EditTeamActivity.this, "Equipo actualizado");
                finish();
            }

            @Override
            public void onError(UtilREST.Response r) {
                CustomToast.show(EditTeamActivity.this, "Error al actualizar equipo");
            }
        });
    }
}