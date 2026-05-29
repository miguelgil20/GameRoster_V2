package com.example.rosterapp.API;

import android.content.Context;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.List;

public class PlayerAdapter extends ArrayAdapter<PlayerModel> {

    public PlayerAdapter(@NonNull Context context,
                         @NonNull List<PlayerModel> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_player, parent, false);
        }

        PlayerModel player = getItem(position);

        TextView txtPlayerNickname = convertView.findViewById(R.id.txtPlayerNickname);
        TextView txtPlayerName = convertView.findViewById(R.id.txtPlayerName);
        TextView txtPlayerRole = convertView.findViewById(R.id.txtPlayerRole);
        TextView txtPlayerCountry = convertView.findViewById(R.id.txtPlayerCountry);

        if (player != null) {
            txtPlayerNickname.setText(player.getNickname());
            txtPlayerName.setText("Nombre: " + player.getName());
            txtPlayerRole.setText("Rol: " + player.getRole());
            txtPlayerCountry.setText("País: " + player.getCountry());
        }

        return convertView;
    }
}