package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.google.android.material.tabs.TabLayout;
import com.prolifera.app.Fragments.EtapaAmostraFragment;
import com.prolifera.app.Fragments.EtapaMainFragment;
import com.prolifera.app.Fragments.EtapaMetricsFragment;
import com.prolifera.app.Fragments.QualifierFragment;
import com.prolifera.app.Fragments.QuantifierFragment;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.TabAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EtapaMainActivity extends AppCompatActivity {

    private TextView tvEtapaMainGen, tvEtapaMainName,tvUserLoggedEtapaMain;
    private ViewPager vpEtapa;
    private TabLayout tabEtapa;

    public EtapaDTO etapa;
    public Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapa_main);

        etapa = (EtapaDTO)getIntent().getExtras().get("etapa");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvUserLoggedEtapaMain = findViewById(R.id.tvUserLoggedEtapaMain);
        vpEtapa = findViewById(R.id.vpEtapa);
        tvEtapaMainGen = findViewById(R.id.tvEtapaMainGen);
        tvEtapaMainName = findViewById(R.id.tvEtapaMainName);
        tabEtapa = findViewById(R.id.tabEtapa);

        tvUserLoggedEtapaMain.setText("Logado como: "+ usuario.getNome());
        tvEtapaMainName.setText(etapa.getNome());
        tvEtapaMainGen.setText(etapa.getProcesso().getLote()+" - "+etapa.getCodigo());

        attachFragments();
    }


    public void attachFragments() {
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new EtapaMainFragment(), "Principal");
        tabAdapter.add(new EtapaAmostraFragment(), "Amostras");
        tabAdapter.add(new EtapaMetricsFragment(), "Métricas");

        vpEtapa.setAdapter(tabAdapter);
        tabEtapa.setupWithViewPager(vpEtapa);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachFragments();
    }
}
