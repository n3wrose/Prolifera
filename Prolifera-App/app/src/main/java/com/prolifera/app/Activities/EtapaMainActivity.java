package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.prolifera.R;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;

public class EtapaMainActivity extends AppCompatActivity {

    private EtapaDTO etapa;
    private Usuario usuario;
    private TextView tvEtapaNameEtapaMain, tvUserLoggedEtapaMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapa_main);

        tvEtapaNameEtapaMain = findViewById(R.id.tvEtapaNameEtapaMain);
        tvUserLoggedEtapaMain = findViewById(R.id.tvUserLoggedEtapaMain);

        etapa = (EtapaDTO)getIntent().getExtras().get("etapa");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvEtapaNameEtapaMain.setText(etapa.getCodigo()+" - "+etapa.getNome());
        tvUserLoggedEtapaMain.setText("Logado como: "+usuario.getNome());


    }

    public void novaMetrica(View view) {
        Intent intent = new Intent(EtapaMainActivity.this, CreateMetricsActivity.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("etapa",etapa);
        startActivity(intent);
    }
}
