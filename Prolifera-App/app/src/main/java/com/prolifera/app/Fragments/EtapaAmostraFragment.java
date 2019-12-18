package com.prolifera.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EtapaAmostraFragment extends Fragment {

    private ListView lstEtapaAmostras;
    private List<AmostraDTO> amostras;
    private RequestQueue rq;
    private Button btnEtapaReport;
    private TextView tvCreateMetric;
    private EtapaDTO etapa;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etapa_amostras, container, false);

        lstEtapaAmostras = view.findViewById(R.id.lstEtapaAmostras);
        btnEtapaReport = view.findViewById(R.id.btnEtapaReport);

        etapa = ((EtapaMainActivity)getActivity()).etapa;
        usuario = ((EtapaMainActivity)getActivity()).usuario;

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        lstEtapaAmostras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ItemDataActivity.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("amostra",amostras.get(i));
                startActivity(intent);
            }
        });

        btnEtapaReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report();
            }
        });

        fillSampleList();
        return view;
    }

    private void report(){
        String url = getResources().getString(R.string.server_address) + "report/" + etapa.getIdEtapa();
        Toast.makeText(getContext(),"Relatório está sendo processado!",Toast.LENGTH_SHORT).show();
        JsonArrayRequest amostraRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null)
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(amostraRequest);
    }

    private void fillSampleList() {
        amostras = new ArrayList<>();
        Toast.makeText(getContext(),"Preenchendo lista de amostras...",Toast.LENGTH_SHORT).show();
        final ArrayAdapter<String> amostraAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        lstEtapaAmostras.setAdapter(amostraAdapter);
        String url = getResources().getString(R.string.server_address) + "amostra/etapa/" + etapa.getIdEtapa();
        JsonArrayRequest amostraRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.equals(null)) return;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                AmostraDTO a = JsonParser.parseAmostra(obj);
                                if (a != null) {
                                    amostras.add(a);
                                    amostraAdapter.add("Amostra Id "+a.getIdAmostra()+": "+a.getNome());
                                } else {
                                    Log.e("error","Amostra corrompida!");
                                    Toast.makeText(getContext(),"Alerta de Amostra corrompida",Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (amostraAdapter.isEmpty())
                            amostraAdapter.add("Esta etapa não possui amostras.");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(amostraRequest);
    }

}
