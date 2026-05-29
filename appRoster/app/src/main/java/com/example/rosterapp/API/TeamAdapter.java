package com.example.rosterapp.API;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.rosterapp.R;

import java.util.List;

public class TeamAdapter extends ArrayAdapter<TeamModel> {

    public TeamAdapter(@NonNull Context context,
                       @NonNull List<TeamModel> teams) {
        super(context, 0, teams);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_team, parent, false);
        }

        TeamModel team = getItem(position);

        TextView txtTeamName = convertView.findViewById(R.id.txtTeamName);
        TextView txtTeamRegion = convertView.findViewById(R.id.txtTeamRegion);
        TextView txtTeamRanking = convertView.findViewById(R.id.txtTeamRanking);

        if (team != null) {
            txtTeamName.setText(team.getName());
            txtTeamRegion.setText("Región: " + team.getRegion());
            txtTeamRanking.setText("Ranking: " + team.getRanking());
        }

        return convertView;
    }
}