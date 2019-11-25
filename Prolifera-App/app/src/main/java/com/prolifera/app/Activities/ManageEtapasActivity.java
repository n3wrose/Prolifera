package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ManageEtapasActivity extends AppCompatActivity {

    private TextView tvUserLoggedManageEtapas, tvEtapasListTitle;
    private ListView lstEtapasFinalizadas, lstEtapasEmAndamento, lstEtapasEmEspera;
    private Usuario usuario;
    private ProcessoDTO processo;
    private List<EtapaDTO> etapas;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_etapas);

        //component attribution
        tvUserLoggedManageEtapas = findViewById(R.id.tvUserLoggedManageEtapas);
        tvEtapasListTitle = findViewById(R.id.tvEtapasListTitle);
        lstEtapasEmAndamento = findViewById(R.id.lstEtapasEmAndamento);
        lstEtapasFinalizadas = findViewById(R.id.lstEtapasFinalizadas);
        lstEtapasEmEspera = findViewById(R.id.lstEtapasEmEspera);

        // intent parameters retrieval
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvEtapasListTitle.setText("ETAPAS - " + processo.getLote());
        tvUserLoggedManageEtapas.setText("Logado como: "+usuario.getNome());
        updateEtapas();
    }

    private void updateEtapas() {

        etapas = new ArrayList<>();
        final ArrayAdapter<String> finalizadasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstEtapasFinalizadas.setAdapter(finalizadasAdapter);
        final ArrayAdapter<String> andamentoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstEtapasEmAndamento.setAdapter(andamentoAdapter);
        final ArrayAdapter<String> esperaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstEtapasEmEspera.setAdapter(esperaAdapter);

        lstEtapasEmAndamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ManageEtapasActivity.this,EtapaMainActivity.class);
                intent.putExtra("etapa",etapas.get(i));
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                updateEtapas();
            }
        });
        lstEtapasEmEspera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ManageEtapasActivity.this,EtapaMainActivity.class);
                intent.putExtra("etapa",etapas.get(i+andamentoAdapter.getCount()));
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                updateEtapas();
            }
        });
        lstEtapasFinalizadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ManageEtapasActivity.this,EtapaMainActivity.class);
                intent.putExtra("etapa",etapas.get(i+andamentoAdapter.getCount()+esperaAdapter.getCount()));
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                updateEtapas();
            }
        });

        //setting up ArrayRequest
        String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/etapa/"+processo.getIdProcesso();

        JsonArrayRequest processosRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals(null)) return;
                        int i;
                        for (i = 0; i<response.length(); i++) {
                            try {
                                EtapaDTO edto = JsonParser.parseEtapa(response.getJSONObject(i));
                                if (edto == null) {
                                    Log.i("error","etapa at index "+i+" is null");
                                    continue;
                                }
                                etapas.add(edto);
                                switch (edto.getStatus()) {
                                    case EtapaDTO.STATUS_EM_ANDAMENTO:
                                        andamentoAdapter.add(edto.getCodigo()+" - "+edto.getNome()+ ": Iniciada em "+edto.getDataInicio());
                                        break;
                                    case EtapaDTO.STATUS_EM_ESPERA:
                                        esperaAdapter.add(edto.getCodigo()+" - "+edto.getNome()+ ": Prevista para "+edto.getDataPrevista());
                                    case EtapaDTO.STATUS_FINALIZADO:
                                        finalizadasAdapter.add(edto.getCodigo()+" - "+edto.getNome()+ ": Finalizada em "+edto.getDataFim());
                                }
                            } catch (Exception e) { e.printStackTrace(); }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(processosRequest);
    }

    public void novaEtapa(View view) {
        Intent intent = new Intent(ManageEtapasActivity.this,CreateEtapaActivity.class);
        intent.putExtra("processo", processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
        updateEtapas();
    }
}
