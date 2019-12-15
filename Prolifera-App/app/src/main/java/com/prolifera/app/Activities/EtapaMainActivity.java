package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EtapaMainActivity extends AppCompatActivity {

    private EtapaDTO etapa;
    private Usuario usuario;
    private RequestQueue rq;
    private ListView lstMetric;
    private Button btnAlterarStatus, btnCreateMetric;
    private TextView tvEtapaMainGen, tvUserLoggedEtapaMain, tvEtapaMainName, tvMetricsInfo, tvCreateMetric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapa_main);

        tvEtapaMainGen = findViewById(R.id.tvEtapaMainGen);
        tvEtapaMainName = findViewById(R.id.tvEtapaMainName);
        tvUserLoggedEtapaMain = findViewById(R.id.tvUserLoggedEtapaMain);
        tvMetricsInfo = findViewById(R.id.tvMetricsInfo);
        btnAlterarStatus = findViewById(R.id.btnAlterarStatus);
        btnCreateMetric = findViewById(R.id.btnCreateMetric);
        tvCreateMetric = findViewById(R.id.tvCreateMetric);
        lstMetric = findViewById(R.id.lstMetric);

        etapa = (EtapaDTO)getIntent().getExtras().get("etapa");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvEtapaMainName.setText(etapa.getNome());
        tvEtapaMainGen.setText(etapa.getProcesso().getLote()+" - "+etapa.getCodigo());
        tvUserLoggedEtapaMain.setText("Logado como: "+usuario.getNome());

        rq = RequestQueueSingleton.getInstance(getApplicationContext()).getRequestQueue();

        updateEtapa();
        updateMetrics();
    }

    public void novaMetrica(View view) {
        Intent intent = new Intent(EtapaMainActivity.this, CreateMetricsActivity.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("etapa",etapa);
        startActivity(intent);
        updateMetrics();
    }

    public void alterarStatus(View view) {

        String message = "";
        if (etapa.getStatus() == Etapa.STATUS_EM_ESPERA) {
            message = "Deseja iniciar essa etapa agora? ";
        }
        if (etapa.getStatus() == Etapa.STATUS_EM_ANDAMENTO) {
            message = "Deseja finalizar essa etapa agora?";
        }

        btnAlterarStatus.setEnabled(false);
        new AlertDialog.Builder(this)
                .setTitle("Alterar Status")
                .setMessage(message)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Enviando dados . . .",Toast.LENGTH_SHORT).show();
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
                                        updateEtapa();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Erro ao enviar dados!",  Toast.LENGTH_LONG).show();
                                Log.i("error",error.getMessage());
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
        switch (etapa.getStatus()) {
            case Etapa.STATUS_EM_ESPERA:
                btnAlterarStatus.setText("Status: Em espera");
                break;
            case Etapa.STATUS_EM_ANDAMENTO:
                btnAlterarStatus.setText("Status: Em andamento");
                break;
            case Etapa.STATUS_FINALIZADO:
                btnAlterarStatus.setText("Status: Finalizado");
                btnAlterarStatus.setEnabled(false);
                btnAlterarStatus.setBackgroundColor(Color.parseColor("#C35F5050"));
                btnCreateMetric.setEnabled(false);
                tvCreateMetric.setEnabled(false);
        }
    }

    private void updateMetrics() {

        final ArrayAdapter<String> metricAdapter = new ArrayAdapter<>(this.getApplicationContext(), android.R.layout.simple_list_item_1);
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
}
