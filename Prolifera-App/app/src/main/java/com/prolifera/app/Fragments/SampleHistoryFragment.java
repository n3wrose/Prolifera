package com.prolifera.app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SampleHistoryFragment extends Fragment {

    private ListView lstHistoryMeasure, lstRecentMeasure;
    private Button btnAtualizar;
    private AmostraDTO amostra;
    private RequestQueue rq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample_history, container, false);

        lstHistoryMeasure = view.findViewById(R.id.lstHistoryMeasure);
        lstRecentMeasure = view.findViewById(R.id.lstRecentMeasure);
        btnAtualizar = view.findViewById(R.id.btnReport);

        amostra = ((ItemDataActivity)getActivity()).amostra;

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        updateLists();


        btnAtualizar.setVisibility(View.INVISIBLE);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLists();
            }
        });

        return view;
    }

    private void updateLists() {

        final ArrayAdapter<String> historicAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        lstHistoryMeasure.setAdapter(historicAdapter);
        final ArrayAdapter<String> recentAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        lstRecentMeasure.setAdapter(recentAdapter);

        String url = getResources().getString(R.string.server_address) + "amostra/"+amostra.getIdAmostra();
        JsonObjectRequest sampleRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            AmostraDTO adto = JsonParser.parseAmostra(response);
                            if (adto != null) {
                                List<Long> ids = new ArrayList<>();
                                for (AmostraQuantificadorDTO aqdto : amostra.getMedidas()) {
                                    historicAdapter.add(aqdto.getTexto()+ " - "+ Utils.toUserDateFull(aqdto.getTimestamp()));
                                    if (!ids.contains((Long)aqdto.getQuantificador().getIdQuantificador())) {
                                        recentAdapter.add(aqdto.getTexto());
                                        ids.add(aqdto.getQuantificador().getIdQuantificador());
                                    }
                                }
                                ids = new ArrayList<>();
                                for (AmostraQualificadorDTO aqdto : amostra.getClassificacoes()) {
                                    historicAdapter.add(aqdto.getTexto()+ " - "+Utils.toUserDateFull(aqdto.getTimestamp()));
                                    if (!ids.contains((Long)aqdto.getQualificadorDTO().getIdQualificador())){
                                        recentAdapter.add(aqdto.getTexto());
                                        ids.add(aqdto.getQualificadorDTO().getIdQualificador());
                                    }
                                }
                            }
                        }
                        if (historicAdapter.isEmpty()) historicAdapter.add("Não há medidas!");
                        if (recentAdapter.isEmpty()) recentAdapter.add("Sem medidas recentes.");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.getMessage());

                    }
                });
        rq.add(sampleRequest);


    }
}
