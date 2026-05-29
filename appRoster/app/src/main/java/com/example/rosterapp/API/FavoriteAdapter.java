package com.example.rosterapp.API;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.rosterapp.R;

import java.util.List;

public class FavoriteAdapter extends ArrayAdapter<FavoriteModel> {

    public FavoriteAdapter(@NonNull Context context,
                           @NonNull List<FavoriteModel> favorites) {
        super(context, 0, favorites);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_favorite, parent, false);
        }

        FavoriteModel favorite = getItem(position);

        TextView txtFavoriteTeamName = convertView.findViewById(R.id.txtFavoriteTeamName);
        TextView txtFavoriteRegion = convertView.findViewById(R.id.txtFavoriteRegion);
        TextView txtFavoriteRanking = convertView.findViewById(R.id.txtFavoriteRanking);

        if (favorite != null) {
            txtFavoriteTeamName.setText(favorite.getTeamName());
            txtFavoriteRegion.setText("Región: " + favorite.getRegion());
            txtFavoriteRanking.setText("Ranking: " + favorite.getRanking());
        }

        return convertView;
    }
}