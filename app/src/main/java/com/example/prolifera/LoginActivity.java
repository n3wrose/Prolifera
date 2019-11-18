package com.example.prolifera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvInfoLogin;

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvInfoLogin = (TextView) findViewById(R.id.tvInfoLogin);

    }

    public void login(View view) {

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        new LoginAsyncTask().execute();

    }

    private void loginYeet() {
        tvInfoLogin.setText("Conectado!");
    }

    private void loginNah() {
        tvInfoLogin.setText("Erro na conexão");
    }


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
    }
}