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
        String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/user/" + username.replace("'", "");

        JsonObjectRequest loginRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response.equals(null))
                    loginNah("Usuário não encontrado!");
                else {
                    usuario = JsonParser.parseUser(response);
                    if (usuario.getSenha().equals(password))
                        loginYeet();
                    else
                        loginNah("Senha incorreta, tente novamente");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               loginNah(error.getMessage());

            }
        });

        tvInfoLogin.setText("Conectando...");
        tvInfoLogin.setVisibility(View.VISIBLE);
        rq.add(loginRequest);

    }

    private void loginYeet() {
        tvInfoLogin.setText("Conectado!");
        try {
            Thread.sleep(500);
        } catch (Exception e) { }
        Intent intent = new Intent(LoginActivity.this, GenSelectionActivity.class);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
        etPassword.setText("");
        etUsername.setText("");
        tvInfoLogin.setText("");
    }

    private void loginNah(String message) {
        tvInfoLogin.setText(message);
    }
/*
    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            tvInfoLogin.setText("Conectando...");
            tvInfoLogin.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... voids) {

            Log.d(LOG_TAG, "User: " + username + ", Pass: " + password);


            //Esse bloco try-catch abaixo soh espera 1 segundo para ir para onPostExecute
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Esse return abaixo eh o argumento "result" de "onPostExecute"

            //return "some_random_string";
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null)
                loginYeet();
            else
                loginNah();

        }
    }*/
}