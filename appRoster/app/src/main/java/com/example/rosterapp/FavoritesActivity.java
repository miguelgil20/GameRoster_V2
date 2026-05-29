package com.example.rosterapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;


import com.example.rosterapp.API.FavoriteModel;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ListView listFavorites;
    private ArrayList<FavoriteModel> favorites;
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listFavorites = findViewById(R.id.listFavorites);

        favorites = new ArrayList<>();
        adapter = new FavoriteAdapter(this, favorites);
        listFavorites.setAdapter(adapter);

        registerForContextMenu(listFavorites);

        cargarFavoritos();
    }

    private void cargarFavoritos() {
        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        Long userId = getSharedPreferences("prefs", MODE_PRIVATE)
                .getLong("userId", 1L);

        API.getFavoritesByUser(userId, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                favorites.clear();
                favorites.addAll(UtilJSONParser.parseArrayFavorites(r.content));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(FavoritesActivity.this, "Error cargando favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Opciones");
        menu.add(0, 1, 0, "Eliminar favorito");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        FavoriteModel favorite = favorites.get(info.position);

        if (item.getItemId() == 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar")
                    .setMessage("¿Quieres eliminar este favorito?")
                    .setPositiveButton("Sí", (dialog, which) -> eliminarFavorito(favorite.getId()))
                    .setNegativeButton("No", null)
                    .show();

            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void eliminarFavorito(Long id) {
        String token = getSharedPreferences("prefs", MODE_PRIVATE)
                .getString("token", "");

        API.deleteFavorite(id, token, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                Toast.makeText(FavoritesActivity.this, "Favorito eliminado", Toast.LENGTH_SHORT).show();
                cargarFavoritos();
            }

            @Override
            public void onError(UtilREST.Response r) {
                Toast.makeText(FavoritesActivity.this, "Error eliminando favorito", Toast.LENGTH_SHORT).show();
            }
        });
    }
}