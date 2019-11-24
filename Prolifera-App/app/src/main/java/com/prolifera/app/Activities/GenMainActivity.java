package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prolifera.R;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.ProcessoDTO;

public class GenMainActivity extends AppCompatActivity {

    private ProcessoDTO processo ;
    private Usuario usuario;
    private Button btnGerarLote, btnVerGrafo, btnEtapas, btnBuscarAmostra;
    private TextView tvGenNameGenMain, tvUserLoggedGenMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_main);

        //component attribution
        tvGenNameGenMain = findViewById(R.id.tvGenNameGenMain);
        tvUserLoggedGenMain = findViewById(R.id.tvUserLoggedGenMain);
        btnGerarLote = findViewById(R.id.btnGerarLote);
        btnVerGrafo = findViewById(R.id.btnVerGrafo);
        btnEtapas = findViewById(R.id.btnEtapas);
        btnBuscarAmostra = findViewById(R.id.btnBuscarAmostra);

        // intent parameters retrieval
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvGenNameGenMain.setText(processo.getLote());
        tvUserLoggedGenMain.setText(("Logado como: "+usuario.getNome()));
    }

    public void etapa(View view) {
        Intent intent = new Intent(GenMainActivity.this, ManageEtapasActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void gerarLote(View view) {
        Intent intent = new Intent(GenMainActivity.this, GenerateBatchActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void verGrafo(View view) {

    }

    public void buscarAmostra(View view) {

    }
}
