package com.example.rosterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.res.Configuration;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rosterapp.API.API;
import com.example.rosterapp.API.TeamAdapter;
import com.example.rosterapp.API.TeamModel;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TeamActivity extends AppCompatActivity {

    private ListView listTeams;
    private Spinner spinnerRegion;
    private CheckBox checkTopTeams;
    private RadioGroup radioRanking;
    private Button btnFilter;

    private ArrayList<TeamModel> teams;
    private ArrayList<TeamModel> allTeams;
    private TeamAdapter adapter;

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
        setContentView(R.layout.activity_team);

        listTeams = findViewById(R.id.listTeams);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        checkTopTeams = findViewById(R.id.checkTopTeams);
        radioRanking = findViewById(R.id.radioRanking);
        btnFilter = findViewById(R.id.btnFilter);

        teams = new ArrayList<>();
        allTeams = new ArrayList<>();

        adapter = new TeamAdapter(this, teams);
        listTeams.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Todas", "EU", "NA", "LATAM", "KR", "APAC", "BR"}
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(spinnerAdapter);

        registerForContextMenu(listTeams);

        listTeams.setOnItemClickListener((parent, view, position, id) -> {
            TeamModel team = teams.get(position);

            Intent intent = new Intent(this, TeamDetailActivity.class);
            intent.putExtra("TEAM_ID", team.getId());
            intent.putExtra("TEAM_NAME", team.getName());
            intent.putExtra("TEAM_REGION", team.getRegion());
            intent.putExtra("TEAM_RANKING", team.getRanking());

            startActivity(intent);
        });

        btnFilter.setOnClickListener(v -> aplicarFiltros());

        cargarEquipos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEquipos();
    }

    private void cargarEquipos() {
        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        API.getTeams(token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                allTeams.clear();
                allTeams.addAll(UtilJSONParser.parseArrayTeams(r.content));

                aplicarFiltros();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(
                        TeamActivity.this,
                        "Error cargando equipos",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void aplicarFiltros() {
        teams.clear();

        String regionSeleccionada = spinnerRegion.getSelectedItem().toString();
        boolean soloTop = checkTopTeams.isChecked();

        for (TeamModel team : allTeams) {
            boolean cumpleRegion =
                    regionSeleccionada.equals("Todas")
                            || team.getRegion().equalsIgnoreCase(regionSeleccionada);

            boolean cumpleTop =
                    !soloTop
                            || team.getRanking() <= 3;

            if (cumpleRegion && cumpleTop) {
                teams.add(team);
            }
        }

        int radioSeleccionado = radioRanking.getCheckedRadioButtonId();

        if (radioSeleccionado == R.id.radioAsc) {
            Collections.sort(teams, Comparator.comparingInt(TeamModel::getRanking));
        } else if (radioSeleccionado == R.id.radioDesc) {
            Collections.sort(teams, (t1, t2) ->
                    Integer.compare(t2.getRanking(), t1.getRanking())
            );
        }

        adapter.notifyDataSetChanged();

        Toast.makeText(
                this,
                "Filtros aplicados",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Opciones del equipo");
        menu.add(0, 1, 0, "Compartir equipo");
        menu.add(0, 2, 1, "Abrir web Valorant");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        TeamModel team = teams.get(info.position);

        if (item.getItemId() == 1) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Mira este equipo: " + team.getName());
            startActivity(Intent.createChooser(share, "Compartir"));
            return true;
        }

        if (item.getItemId() == 2) {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vlr.gg"));
            startActivity(browser);
            return true;
        }

        return super.onContextItemSelected(item);
    }
}