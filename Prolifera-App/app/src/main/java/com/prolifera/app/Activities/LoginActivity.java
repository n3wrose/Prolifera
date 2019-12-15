package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.JsonParser;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvInfoLogin;
    private Usuario usuario;
    private RequestQueue rq ;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //component attribution
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvInfoLogin = (TextView) findViewById(R.id.tvInfoLogin);

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

    }

    public void login(View view) {

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        String url = getResources().getString(R.string.server_address) + "user/" + username.replace("'", "");

        JsonObjectRequest loginRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    usuario = JsonParser.parseUser(response);
                    if (usuario.getSenha().equals(password))
                        loginYeet();
                    else
                        loginNah("Senha incorreta, tente novamente");
                    } else  loginNah("Usuário não encontrado!");
                }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               loginNah(error.getMessage());

            }
        });

        Toast.makeText(LoginActivity.this, "Conectando . . .", Toast.LENGTH_SHORT).show();
        //tvInfoLogin.setVisibility(View.VISIBLE);
        rq.add(loginRequest);

    }

    private void loginYeet() {
        /*Toast.makeText(LoginActivity.this, "Conectado!", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(500);
        } catch (Exception e) { }*/

        Intent intent = new Intent(LoginActivity.this, GenSelectionActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
        etPassword.setText("");
        etUsername.setText("");
        tvInfoLogin.setText("");
    }

    private void loginNah(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}