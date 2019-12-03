package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prolifera.R;
import com.prolifera.app.Model.DB.Usuario;

public class CreateMetricsActivity extends AppCompatActivity {

    private TextView tvUserLoggedCreateMetrics;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_metrics);

        tvUserLoggedCreateMetrics = findViewById(R.id.tvUserLoggedCreateMetrics);

        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvUserLoggedCreateMetrics.setText("Logado como: "+ usuario.getNome());
    }
}
