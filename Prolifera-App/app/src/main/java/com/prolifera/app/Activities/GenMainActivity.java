package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

public class GenMainActivity extends AppCompatActivity {

    private RequestQueue rq;
    private ProcessoDTO processo ;
    private Usuario usuario;
    private Button btnGerarLote, btnVerGrafo, btnEtapas, btnBuscarAmostra;
    private TextView tvGenNameGenMain, tvUserLoggedGenMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_main);

        //component attribution
        tvGenNameGenMain = findViewById(R.id.tvGenNameGenMain);
        tvUserLoggedGenMain = findViewById(R.id.tvUserLoggedGenMain);
        btnGerarLote = findViewById(R.id.btnGerarLote);
        btnVerGrafo = findViewById(R.id.btnVerGrafo);
        btnEtapas = findViewById(R.id.btnEtapas);
        btnBuscarAmostra = findViewById(R.id.btnBuscarAmostra);

        // intent parameters retrieval
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvGenNameGenMain.setText(processo.getLote());
        tvUserLoggedGenMain.setText(("Logado como: "+usuario.getNome()));
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

    public void verGrafo(View view) {

    }

    public void buscarAmostra(View view) {

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
        String qrcode;
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            qrcode = result.getContents();
            Log.i("qrcode",qrcode);
            String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/amostra/"+qrcode;
            JsonObjectRequest loginRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                AmostraDTO adto = JsonParser.parseAmostra(response);
                                if (adto != null) {
                                    Intent intent = new Intent(GenMainActivity.this, ItemDataActivity.class);
                                    intent.putExtra("amostra",adto);
                                    intent.putExtra("usuario",usuario);
                                    startActivity(intent);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR",error.getMessage());

                        }
                    });
            rq.add(loginRequest);
        }
    }
}
