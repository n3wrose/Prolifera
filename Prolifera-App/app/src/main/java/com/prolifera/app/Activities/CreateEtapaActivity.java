package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prolifera.R;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.ProcessoDTO;

public class CreateEtapaActivity extends AppCompatActivity {

    private TextView tvUserLoggedCreateEtapa;
    private ProcessoDTO processo;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_etapa);

        //component attribution
        tvUserLoggedCreateEtapa = findViewById(R.id.tvUserLoggedCreateEtapa);

        //intent parameters retrieval
        processo = (ProcessoDTO) getIntent().getExtras().get("processo");
        usuario = (Usuario) getIntent().getExtras().get("usuario");

        tvUserLoggedCreateEtapa.setText("Logado como "+ usuario.getNome());
    }
}
