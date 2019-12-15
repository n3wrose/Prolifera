package com.prolifera.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.CreateEtapaActivity;
import com.prolifera.app.Activities.CreateMetricsActivity;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.Activities.ManageEtapasActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Qualificador;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.RequestQueueSingleton;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class QuantifierFragment extends Fragment {

    private TextView tvEnterQuantifierName, tvNewMetricsQuantifier,tvEnterQuantifierUnity;
    private Button  btnCreateQuantifier;
    private EditText etQuantifierUnity, etQuantifierName;
    private RequestQueue rq;
    private EtapaDTO etapa;

    public QuantifierFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_quantifier, container, false);

        tvNewMetricsQuantifier = view.findViewById(R.id.tvNewMetricsQuantifier);
        etQuantifierName = view.findViewById(R.id.etQuantifierName);
        etQuantifierUnity = view.findViewById(R.id.etQuantifierUnity);

        btnCreateQuantifier = view.findViewById(R.id.btnCreateQuantifier);
        tvEnterQuantifierName = view.findViewById(R.id.tvEnterQuantifierName);
        tvEnterQuantifierUnity = view.findViewById(R.id.tvEnterQuantifierUnity);

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        etapa = ((CreateMetricsActivity)getActivity()).etapa ;

        btnCreateQuantifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });

        return view;
    }

    public void create() {
        try {
            Toast.makeText(getContext(),"Enviando dados . .  .",Toast.LENGTH_SHORT).show();
            btnCreateQuantifier.setEnabled(false);
            final Quantificador q = new Quantificador();
            q.setNome(etQuantifierName.getText().toString());
            q.setUnidade(etQuantifierUnity.getText().toString());
            q.setIdEtapa(etapa.getIdEtapa());
            String url = getResources().getString(R.string.server_address)+"quantificador" ;
            JsonObjectRequest newQuantifierRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    btnCreateQuantifier.setEnabled(true);
                    Quantificador qt = new Quantificador();
                    if (response != null) {
                        qt = JsonParser.parseQuantificador(response);
                        if (!qt.equals(null)) {
                            Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            etQuantifierName.setText("");
                            etQuantifierUnity.setText("");
                        }
                    } else
                        Toast.makeText(getContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Erro ao enviar dados: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    btnCreateQuantifier.setEnabled(true);
                }

            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody()  {
                    return q.fillPayload().getBytes();
                }
            };

            rq.add(newQuantifierRequest);

        }
        catch (Exception e) {
            Toast.makeText(getContext(),"Erro ao enviar dados.",Toast.LENGTH_SHORT).show();
            Log.i("error",e.getMessage());
        }
    }
}
