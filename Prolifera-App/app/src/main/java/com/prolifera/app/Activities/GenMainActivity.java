package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class GenMainActivity extends AppCompatActivity {

    private RequestQueue rq;
    private ProcessoDTO processo ;
    private Usuario usuario;
    private Button btnGerarLote, btnVerGrafo, btnEtapas, btnBuscarAmostra;
    private TextView tvGenNameGenMain, tvUserLoggedGenMain, tvGenMainSubtitle;
    static final int READ_CODE_REQUEST = 49374;
    private ImageView imgTesteQR;
    String samples, etapas;
    boolean samplesReady = false, etapasReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_main);

        //component attribution
        tvGenNameGenMain = findViewById(R.id.tvGenNameGenMain);
        tvUserLoggedGenMain = findViewById(R.id.tvUserLoggedGenMain);
        btnGerarLote = findViewById(R.id.btnGerarLote);
        btnVerGrafo = findViewById(R.id.btnGenReport);
        btnEtapas = findViewById(R.id.btnEtapas);
        tvGenMainSubtitle = findViewById(R.id.tvGenMainSubtitle);
        btnBuscarAmostra = findViewById(R.id.btnSampleScanner);

        // intent parameters retrieval
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvGenNameGenMain.setText(processo.getLote());
        tvUserLoggedGenMain.setText(("Logado como: "+usuario.getNome()));

        tvGenMainSubtitle.setText("Iniciado em "+Utils.toUserDate(processo.getTimestamp()));
    }

    public void etapa(View view) {
        Intent intent = new Intent(GenMainActivity.this, ManageEtapasActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void gerarLote(View view) {
        Intent intent = new Intent(GenMainActivity.this, GenerateBatchActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }


    public void scanner(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Apontar para o QR code contendo o número da amostra");
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setCameraId(0);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        String qrcode = null;
        if (requestCode == READ_CODE_REQUEST && resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null ) {
                qrcode = result.getContents();
                queueSample(qrcode);
            }
        }

    }

    public void buscarAmostra(View view) {
        //humble builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Código não identificado. Deseja digitar o Id da amostra?");
        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Id da Amostra");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                queueSample(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void queueSample(String id) {
        String url = getResources().getString(R.string.server_address) + "amostra/"+id;
        JsonObjectRequest sampleRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            AmostraDTO adto = JsonParser.parseAmostra(response);
                            if (adto != null) {
                                Intent intent = new Intent(GenMainActivity.this, ItemDataActivity.class);
                                intent.putExtra("amostra", adto);
                                intent.putExtra("usuario", usuario);
                                startActivity(intent);
                            } else
                                Toast.makeText(getApplicationContext(),"Amostra corrompida!",Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(),"Amostra não encontrada!",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",""+error.getMessage());
                    }
                });
        rq.add(sampleRequest);
    }
/*
    @Override
    public void onResume()
    {
        updateSubtitle();
        super.onResume();
    }*/

    private void updateSubtitle() {

        etapasReady = false;
        samplesReady = false;
        String url = getResources().getString(R.string.server_address) + "processo/count_samples/"+processo.getIdProcesso();
        JsonObjectRequest sampleRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            samples = Integer.parseInt(response.toString())+" amostras registradas;";
                        } else samples = "Não foi possível contar amostras;";

                        samplesReady = true;
                        if (etapasReady) {
                            String text = "Iniciado em "+ Utils.toUserDate(processo.getTimestamp())+"\n"
                                    + samples +"\n"+etapas;
                            tvGenMainSubtitle.setText(text);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",""+error.getMessage());
                    }
                });
        rq.add(sampleRequest);

        url = getResources().getString(R.string.server_address) + "etapa/"+processo.getIdProcesso();
        JsonArrayRequest etapasRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            int i, a = 0, e = 0, f= 0 ;
                            for (i = 0; i < response.length(); i++) {
                                try {
                                    EtapaDTO edto = JsonParser.parseEtapa(response.getJSONObject(i));
                                    if (edto == null) {
                                        Log.i("error", "etapa at index " + i + " is null");
                                        continue;
                                    }
                                    switch (edto.getStatus()) {
                                        case EtapaDTO.STATUS_EM_ANDAMENTO:
                                            a ++;
                                            break;
                                        case EtapaDTO.STATUS_EM_ESPERA:
                                            e ++;
                                            break;
                                        case EtapaDTO.STATUS_FINALIZADO:
                                            f ++;
                                    }
                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            }
                            etapas = "Há "+a+" etapas em andamento, "+e+" agendadas e "+f+" finalizadas.";
                        } else etapas = "Não foi possível contar etapas!";


                        etapasReady = true;
                        if (samplesReady) {
                            String text = "Iniciado em "+ Utils.toUserDate(processo.getTimestamp())+"\n"
                                    + samples +"\n"+etapas;
                            tvGenMainSubtitle.setText(text);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(etapasRequest);

    }

    public void emitir(View view) {
        String url = getResources().getString(R.string.server_address) + "report_full/" + processo.getIdProcesso();
        Toast.makeText(getApplicationContext(),"Relatório está sendo processado!",Toast.LENGTH_SHORT).show();
        JsonArrayRequest amostraRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null)
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(amostraRequest);
    }


}
