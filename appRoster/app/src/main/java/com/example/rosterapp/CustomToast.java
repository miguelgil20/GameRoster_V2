package com.example.rosterapp;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosterapp.R;

public class CustomToast {

    public static void show(Activity activity, String mensaje) {

        LayoutInflater inflater = activity.getLayoutInflater();

        View layout = inflater.inflate(
                R.layout.toast_custom,
                activity.findViewById(android.R.id.content),
                false
        );

        TextView txt = layout.findViewById(R.id.txtToast);
        txt.setText(mensaje);

        Toast toast = new Toast(activity);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setView(layout);

        toast.show();
    }

}
