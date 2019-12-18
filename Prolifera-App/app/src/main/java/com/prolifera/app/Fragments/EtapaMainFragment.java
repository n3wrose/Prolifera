package com.prolifera.app.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.CreateMetricsActivity;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

public class EtapaMainFragment extends Fragment {

    private EtapaDTO etapa;
    private Usuario usuario;
    private RequestQueue rq;
    private ListView lstMetric;
    private TextView tvEtapaEquipamento, tvEtapaDescription, tvEtapaDatas, tvEtapaHint;
    private Button btnAlterarStatus;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etapa_main, container, false);

        btnAlterarStatus = view.findViewById(R.id.btnAlterarStatus);
        tvEtapaDatas = view.findViewById(R.id.tvEtapaDatas);
        tvEtapaDescription = view.findViewById(R.id.tvEtapaDescription);
        tvEtapaEquipamento = view.findViewById(R.id.tvEtapaEquipamento);
        tvEtapaHint = view.findViewById(R.id.tvEtapaHint);

        etapa = ((EtapaMainActivity)getActivity()).etapa;
        usuario = ((EtapaMainActivity)getActivity()).usuario;


        btnAlterarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarStatus();
            }
        });

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        updateEtapa();
        
        return view;

    }

    public void alterarStatus() {

        String message = "";
        if (etapa.getStatus() == Etapa.STATUS_EM_ESPERA) {
            message = "Deseja iniciar essa etapa agora? ";
        }
        if (etapa.getStatus() == Etapa.STATUS_EM_ANDAMENTO) {
            message = "Deseja finalizar essa etapa agora?";
        }

        btnAlterarStatus.setEnabled(false);
        new AlertDialog.Builder(getActivity())
                .setTitle("Alterar Status")
                .setMessage(message)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Enviando dados . . .",Toast.LENGTH_SHORT).show();
                        final Etapa e = new Etapa(etapa);
                        e.setStatus(etapa.getStatus()+1);
                        if (e.getStatus() == Etapa.STATUS_EM_ANDAMENTO)
                            e.setDataInicio(new Date());
                        if (e.getStatus() == Etapa.STATUS_FINALIZADO)
                            e.setDataFim(new Date());


                        String url = getResources().getString(R.string.server_address) + "etapa";
                        JsonObjectRequest updateEtapaRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                btnAlterarStatus.setEnabled(true);
                                EtapaDTO edto = new EtapaDTO();
                                if (response != null) {
                                    edto = JsonParser.parseEtapa(response);
                                    if (edto != null) {
                                        etapa = edto;
                                        ((EtapaMainActivity)getActivity()).etapa = etapa;
                                        updateEtapa();
                                    }
                                    else
                                        Toast.makeText(getContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Erro ao enviar dados!",  Toast.LENGTH_LONG).show();
                                //Log.i("error",error.getMessage());
                                btnAlterarStatus.setEnabled(true);
                            }

                        }){

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody()  {
                                return e.fillPayload().getBytes();
                            }
                        };

                        rq.add(updateEtapaRequest);
                    }
                })

                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void updateEtapa() {

        if (etapa.getDescricao().equals("null")||etapa.getDescricao().equals(""))
            tvEtapaDescription.setText("Não possui descrição.");
        else
            tvEtapaDescription.setText(etapa.getDescricao());
        if (etapa.getEquipamento().equals("null"))
            tvEtapaEquipamento.setText("Não possui Equipamento.");
        else
            tvEtapaEquipamento.setText(etapa.getEquipamento());
        String datas = "Previsão: "+etapa.getDataPrevista();
        switch (etapa.getStatus()) {
            case Etapa.STATUS_EM_ESPERA:
                btnAlterarStatus.setText("Status: Em espera");
                break;
            case Etapa.STATUS_EM_ANDAMENTO:
                btnAlterarStatus.setText("Status: Em andamento");
                datas = datas + "\nInício: "+etapa.getDataInicio();
                break;
            case Etapa.STATUS_FINALIZADO:
                btnAlterarStatus.setText("Status: Finalizado");
                btnAlterarStatus.setEnabled(false);
                btnAlterarStatus.setBackgroundColor(Color.parseColor("#C35F5050"));
                tvEtapaHint.setText("");
                datas = datas + "\nInício: "+etapa.getDataInicio();
                datas = datas + "\nFim: "+etapa.getDataFim();
                //btnCreateMetric.setEnabled(false);
                //tvCreateMetric.setEnabled(false);
        }
        tvEtapaDatas.setText(datas);
    }



    public void report(View view) {
        String url = getResources().getString(R.string.server_address) + "report/"+etapa.getIdEtapa();
        Toast.makeText(getContext(), "Exportando dados . . .", Toast.LENGTH_SHORT).show();
        JsonArrayRequest metricsRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.equals(null)) return;

                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(metricsRequest);

    }
}
