package com.prolifera.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.prolifera.R;
import com.prolifera.app.Activities.CreateMetricsActivity;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class EtapaMetricsFragment extends Fragment {

    private ListView lstMetric;
    private RequestQueue rq;
    private Button btnNovaMetrica;
    private TextView tvCreateMetric;
    private EtapaDTO etapa;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etapa_metrics, container, false);

        lstMetric = view.findViewById(R.id.lstMetric);
        btnNovaMetrica = view.findViewById(R.id.btnCreateMetric);
        tvCreateMetric = view.findViewById(R.id.tvCreateMetric);

        etapa = ((EtapaMainActivity)getActivity()).etapa;
        usuario = ((EtapaMainActivity)getActivity()).usuario;

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        btnNovaMetrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novaMetrica();
            }
        });

        tvCreateMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novaMetrica();
            }
        });

        updateMetrics();
        return view;
    }


    private void updateMetrics() {

        final ArrayAdapter<String> metricAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        Toast.makeText(getContext(),"Preenchendo lista de métricas...",Toast.LENGTH_SHORT).show();
        lstMetric.setAdapter(metricAdapter);
        String url = getResources().getString(R.string.server_address) + "metrics/" + etapa.getIdEtapa();
        JsonArrayRequest metricsRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.equals(null)) return;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Quantificador q = JsonParser.parseQuantificador(obj);
                                if (q != null)
                                {
                                    if (q.getUnidade() == "" || q.getUnidade().equals(""))
                                        metricAdapter.add(q.getNome()+", adimensional");
                                    else
                                        metricAdapter.add(q.getNome()+", medido em "+q.getUnidade());
                                }
                                else {
                                    QualificadorDTO qdto = JsonParser.parseQualificador(obj);
                                    if (qdto!=null)
                                    {
                                        String msg = qdto.getNome()+", opções: ";
                                        for (Opcao o : qdto.getOpcoes())
                                            msg = msg + o.getValor()+", ";
                                        metricAdapter.add(msg.substring(0,msg.length()-2));
                                    }
                                }

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
        rq.add(metricsRequest);
    }

    public void novaMetrica() {
        Intent intent = new Intent(getActivity(), CreateMetricsActivity.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("etapa",etapa);
        startActivity(intent);
        updateMetrics();
    }

}
