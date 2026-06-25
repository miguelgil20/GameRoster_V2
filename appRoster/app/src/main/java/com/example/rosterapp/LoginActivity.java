package com.example.rosterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rosterapp.API.API;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";

    private EditText etUsername, etPassword;
    private Button btnLogin, btnGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (!TextUtils.isEmpty(token)) {
            abrirPantallaPrincipal();
            return;
        }

        btnLogin.setOnClickListener(v -> hacerLogin());
        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void hacerLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Introduce el nombre de usuario");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Introduce la contraseña");
            etPassword.requestFocus();
            return;
        }

        cambiarEstadoBoton(true);

        JSONObject loginJson = UtilJSONParser.createLogin(username, password);

        API.login(loginJson, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                cambiarEstadoBoton(false);
                guardarSesion(r.content, username);
            }

            @Override
            public void onError(UtilREST.Response r) {
                cambiarEstadoBoton(false);
                Toast.makeText(LoginActivity.this, obtenerMensajeError(r.content, "Usuario o contraseña incorrectos"), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(String respuesta, String username) {

        if (respuesta == null || respuesta.trim().isEmpty()) {
            CustomToast.show(
                    LoginActivity.this,
                    "Respuesta vacía del servidor"
            );
            return;
        }

        String token = respuesta.trim();

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString("token", token)
                .putString("username", username)
                .apply();

        CustomToast.show(
                LoginActivity.this,
                "Sesión iniciada correctamente"
        );
        abrirPantallaPrincipal();
    }

    private String buscarString(JSONObject json, String... claves) {
        for (String clave : claves) {
            String valor = json.optString(clave, "");
            if (!valor.isEmpty()) {
                return valor;
            }
        }

        JSONObject usuario = json.optJSONObject("user");
        if (usuario == null) {
            usuario = json.optJSONObject("usuario");
        }

        if (usuario != null) {
            for (String clave : claves) {
                String valor = usuario.optString(clave, "");
                if (!valor.isEmpty()) {
                    return valor;
                }
            }
        }

        return "";
    }

    private long buscarLong(JSONObject json, String... claves) {
        for (String clave : claves) {
            if (json.has(clave)) {
                return json.optLong(clave, -1);
            }
        }

        JSONObject usuario = json.optJSONObject("user");
        if (usuario == null) {
            usuario = json.optJSONObject("usuario");
        }

        if (usuario != null) {
            for (String clave : claves) {
                if (usuario.has(clave)) {
                    return usuario.optLong(clave, -1);
                }
            }
        }

        return -1;
    }

    private String obtenerMensajeError(String respuesta, String mensajePorDefecto) {
        if (respuesta == null || respuesta.trim().isEmpty()) {
            return mensajePorDefecto;
        }

        try {
            JSONObject json = new JSONObject(respuesta);
            String mensaje = buscarString(json, "message", "mensaje", "error");
            return mensaje.isEmpty() ? mensajePorDefecto : mensaje;
        } catch (JSONException e) {
            return respuesta;
        }
    }

    private void cambiarEstadoBoton(boolean cargando) {
        btnLogin.setEnabled(!cargando);
        btnGoRegister.setEnabled(!cargando);
        btnLogin.setText(cargando ? "Entrando..." : "Entrar");
    }

    private void abrirPantallaPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
