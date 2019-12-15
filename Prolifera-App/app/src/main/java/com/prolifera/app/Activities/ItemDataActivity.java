package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prolifera.R;
import com.google.android.material.tabs.TabLayout;
import com.prolifera.app.Fragments.AddClassificationFragment;
import com.prolifera.app.Fragments.AddMeasureFragment;
import com.prolifera.app.Fragments.SampleHistoryFragment;
import com.prolifera.app.Fragments.SampleMainFragment;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.TabAdapter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDataActivity extends AppCompatActivity {

    private RequestQueue rq;
    private TextView tvItemNameItemData, tvEtapaItemData, tvUserLoggedItemData;
    private ListView lstMeasurement, lstParentItemData;
    private ViewPager vpItemData;
    private TabLayout tabItemData;
    public AmostraDTO amostra;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        tvItemNameItemData = findViewById(R.id.tvItemNameItemData);
        tvEtapaItemData = findViewById(R.id.tvEtapaItemData);
        tvUserLoggedItemData = findViewById(R.id.tvUserLoggedItemData);
        lstParentItemData = findViewById(R.id.lstParentItemData);

        usuario = (Usuario)getIntent().getExtras().get("usuario");
        amostra = (AmostraDTO)getIntent().getExtras().get("amostra");

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new SampleMainFragment(), "Dados");
        tabAdapter.add(new AddMeasureFragment(), "Medir");
        tabAdapter.add(new AddClassificationFragment(), "Classificar");
        tabAdapter.add(new SampleHistoryFragment(), "Histórico");

        vpItemData = findViewById(R.id.vpItemData);
        vpItemData.setAdapter(tabAdapter);

        tabItemData = findViewById(R.id.tabItemData);
        tabItemData.setupWithViewPager(vpItemData);

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvItemNameItemData.setText("Amostra: " + amostra.getNome());
        tvEtapaItemData.setText(amostra.getEtapa().getCodigo()+ " - "+amostra.getEtapa().getNome());
        tvUserLoggedItemData.setText("Logado como: "+usuario.getNome());




        //updateMetricsList();
    }

    private void updateParentList() {

    }

//    private void updateMetricsList() {
//        final ArrayAdapter<String> metricsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        lstMeasurement.setAdapter(metricsAdapter);
//        for (AmostraQuantificadorDTO am : amostra.getMedidas())
//            metricsAdapter.add(am.getTexto());
//        for (AmostraQualificadorDTO ac : amostra.getClassificacoes())
//            metricsAdapter.add(ac.getTexto());
//    }

}
