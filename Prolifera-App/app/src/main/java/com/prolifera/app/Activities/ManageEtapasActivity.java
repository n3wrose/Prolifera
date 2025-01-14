package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.prolifera.R;
import com.google.android.material.tabs.TabLayout;
import com.prolifera.app.Fragments.ListEtapasFragment;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.TabAdapter;

import java.util.List;

public class ManageEtapasActivity extends AppCompatActivity {

    private TextView tvUserLoggedManageEtapas, tvEtapasListTitle;
    private ListView lstEtapasFinalizadas, lstEtapasEmAndamento, lstEtapasEmEspera;
    public Usuario usuario;
    private ViewPager vpEtapas;
    private TabLayout tabEtapas;
    private TabAdapter tabAdapter;
    public ProcessoDTO processo;
    private List<EtapaDTO> etapas;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_etapas);

        //component attribution
        tvUserLoggedManageEtapas = findViewById(R.id.tvUserLoggedManageEtapas);
        tabEtapas = findViewById(R.id.tabEtapa);
        vpEtapas = findViewById(R.id.vpEtapas);
        tvEtapasListTitle = findViewById(R.id.tvEtapasListTitle); /*
        lstEtapasEmAndamento = findViewById(R.id.lstEtapas);
        lstEtapasFinalizadas = findViewById(R.id.lstEtapasFinalizadas);
        lstEtapasEmEspera = findViewById(R.id.lstEtapasEmEspera);*/

        // intent parameters retrieval
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvEtapasListTitle.setText("ETAPAS - " + processo.getLote());
        tvUserLoggedManageEtapas.setText("Logado como: "+usuario.getNome());

        attachFragments();


       /// updateEtapas();
    }

    /*private void updateEtapas() {

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
        String url = getResources().getString(R.string.server_address) + "etapa/"+processo.getIdProcesso();

        JsonArrayRequest etapasRequest = new JsonArrayRequest
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
                                        break;
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
        rq.add(etapasRequest);
    }*/

    public void novaEtapa(View view) {
        Intent intent = new Intent(ManageEtapasActivity.this,CreateEtapaActivity.class);
        intent.putExtra("processo", processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);

    }

    public void attachFragments() {
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new ListEtapasFragment(Etapa.STATUS_EM_ESPERA), "Espera");
        tabAdapter.add(new ListEtapasFragment(Etapa.STATUS_EM_ANDAMENTO), "Andamento");
        tabAdapter.add(new ListEtapasFragment(Etapa.STATUS_FINALIZADO), "Finalizado");

        vpEtapas.setAdapter(tabAdapter);
        tabEtapas.setupWithViewPager(vpEtapas);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachFragments();
    }
}
