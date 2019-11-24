package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GenerateBatchActivity extends AppCompatActivity {

    private RequestQueue rq;
    private TextView tvUserLoggedGenerateBatch;
    private EditText etDescriptionGenerateBatch, etNumberGenerateBatch, etNumberSubGenerateBatch;
    private Spinner spnEtapa;
    private Usuario usuario;
    private ProcessoDTO processo;
    private List<EtapaDTO> etapas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_batch);

        //component attribution
        tvUserLoggedGenerateBatch = findViewById(R.id.tvUserLoggedGenerateBatch);
        etNumberGenerateBatch = findViewById(R.id.etNumberGenerateBatch);
        spnEtapa = findViewById(R.id.spnEtapa);
        etDescriptionGenerateBatch = findViewById(R.id.etDescriptionGenerateBatch);
        etNumberSubGenerateBatch = findViewById(R.id.etNumberSubGenerateBatch);

        // intent parameters retrieval
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvUserLoggedGenerateBatch.setText("Logado como" + usuario.getNome());
        fillEtapaSpinner();
    }

    public void gerarLote(View view) {
        final Amostra amostra = new Amostra();
        int sampleNumber = Integer.parseInt(etNumberGenerateBatch.getText().toString()),
                subSampleNumber = Integer.parseInt(etNumberSubGenerateBatch.getText().toString());
        amostra.setDescricao(etDescriptionGenerateBatch.getText().toString());
        amostra.setIdEtapa(etapas.get(spnEtapa.getSelectedItemPosition()).getIdEtapa());
        amostra.setUsuario(usuario.getLogin());
        String url =  "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/amostra/"+subSampleNumber;

        for (int i = 0; i<sampleNumber; i++) {
            amostra.setNome(Character.toString((char)(65+i)));
            JsonArrayRequest loteRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    if (response.equals(null)) return;
                    finish();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    return amostra.fillPayload().getBytes();
                }
            };
            rq.add(loteRequest);
        }
    }

    private void fillEtapaSpinner() {
        etapas = new ArrayList<>();
        final ArrayAdapter<String> etapasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spnEtapa.setAdapter(etapasAdapter);

        //setting up ArrayRequest
        String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/etapa/started/"+processo.getIdProcesso();
        JsonArrayRequest etapasRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals(null)) return;
                        int i;
                        for (i = 0; i<response.length(); i++) {
                            try {
                                EtapaDTO edto = JsonParser.parseEtapa(response.getJSONObject(i));
                                etapas.add(edto);
                                etapasAdapter.add(edto.getCodigo()+" - "+edto.getNome());
                            } catch (Exception e) { e.printStackTrace(); }
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
