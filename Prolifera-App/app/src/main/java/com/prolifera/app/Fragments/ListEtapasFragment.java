package com.prolifera.app.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.Activities.ManageEtapasActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class ListEtapasFragment extends Fragment {

    private ListView lstEtapas;
    private RequestQueue rq;

    private int status;
    private List<EtapaDTO> etapas ;
    private Usuario usuario;
    private ProcessoDTO processo;


    public ListEtapasFragment() {
        // Required empty public constructor
    }

    public ListEtapasFragment(int status) {
        this.status = status;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_etapas, container, false);
        lstEtapas = view.findViewById(R.id.lstEtapas);

        processo = ((ManageEtapasActivity)this.getActivity()).processo;
        usuario = ((ManageEtapasActivity)this.getActivity()).usuario;

        rq = RequestQueueSingleton.getInstance(this.getContext()).getRequestQueue();
        updateEtapas();
        return view;
    }

    public void updateEtapas() {

        etapas = new ArrayList<>();

        final ArrayAdapter<String> etapaAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        lstEtapas.setAdapter(etapaAdapter);

        lstEtapas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), EtapaMainActivity.class);
                for (EtapaDTO e : etapas)
                    if (e.getStatus() == status)
                        if (i == 0) {
                            intent.putExtra("etapa", e);
                            break;
                        } else
                            i--;
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                updateEtapas();
            }
        });

        //setting up ArrayRequest
        String url = getResources().getString(R.string.server_address) + "etapa/" + processo.getIdProcesso();

        JsonArrayRequest etapasRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals(null)) return;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                EtapaDTO edto = JsonParser.parseEtapa(response.getJSONObject(i));
                                if (edto == null) {
                                    Log.i("error", "etapa at index " + i + " is null");
                                    continue;
                                }
                                etapas.add(edto);

                                if(edto.getStatus() == status && status== Etapa.STATUS_FINALIZADO)
                                    etapaAdapter.add(edto.getCodigo() + " - " + edto.getNome() + ": Finalizada em "+ edto.getDataFim());
                                if(edto.getStatus() == status && status== Etapa.STATUS_EM_ANDAMENTO)
                                    etapaAdapter.add(edto.getCodigo() + " - " + edto.getNome() + ": Iniciada em "+ edto.getDataInicio());
                                if(edto.getStatus() == status && status== Etapa.STATUS_EM_ESPERA)
                                    etapaAdapter.add(edto.getCodigo() + " - " + edto.getNome() + ": Prevista para "+ edto.getDataPrevista());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(etapasRequest);

    }
}
