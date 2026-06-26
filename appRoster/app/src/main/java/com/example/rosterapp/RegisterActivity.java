package com.example.rosterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rosterapp.API.API;
import com.example.rosterapp.API.UtilJSONParser;
import com.example.rosterapp.API.UtilREST;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);

        btnRegister.setOnClickListener(v -> hacerRegistro());
        btnGoLogin.setOnClickListener(v -> abrirLogin());
    }

    private void hacerRegistro() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validarDatos(username, email, password, confirmPassword)) {
            return;
        }

        cambiarEstadoBoton(true);

        JSONObject registerJson = UtilJSONParser.createRegister(username, password, email);

        API.register(registerJson, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                cambiarEstadoBoton(false);
                CustomToast.show(
                        RegisterActivity.this,
                        "Registro completado. Inicia sesión"
                );
                abrirLogin();
            }

            @Override
            public void onError(UtilREST.Response r) {
                cambiarEstadoBoton(false);
                Toast.makeText(RegisterActivity.this, obtenerMensajeError(r.content, "No se pudo registrar el usuario"), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validarDatos(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            etUsername.setError("Introduce el nombre de usuario");
            etUsername.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Introduce el correo");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Correo no válido");
            etEmail.requestFocus();
            return false;
        }

        if (!esPasswordValida(password)) {
            etPassword.setError("8-16 caracteres, mayúscula, minúscula, número y símbolo");
            etPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean esPasswordValida(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }

        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;
        boolean tieneSimbolo = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            } else if (Character.isLowerCase(c)) {
                tieneMinuscula = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            } else {
                tieneSimbolo = true;
            }
        }

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneSimbolo;
    }

    private String obtenerMensajeError(String respuesta, String mensajePorDefecto) {
        if (respuesta == null || respuesta.trim().isEmpty()) {
            return mensajePorDefecto;
        }

        try {
            JSONObject json = new JSONObject(respuesta);
            String mensaje = json.optString("message", "");
            if (mensaje.isEmpty()) {
                mensaje = json.optString("mensaje", "");
            }
            if (mensaje.isEmpty()) {
                mensaje = json.optString("error", "");
            }
            return mensaje.isEmpty() ? mensajePorDefecto : mensaje;
        } catch (JSONException e) {
            return respuesta;
        }
    }

    private void cambiarEstadoBoton(boolean cargando) {
        btnRegister.setEnabled(!cargando);
        btnGoLogin.setEnabled(!cargando);
        btnRegister.setText(cargando ? "Registrando..." : "Registrarse");
    }

    private void abrirLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
