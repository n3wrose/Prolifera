package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prolifera.R;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.Fragments.AddClassificationFragment;
import com.prolifera.app.Fragments.AddMeasureFragment;
import com.prolifera.app.Fragments.EtapaAmostraFragment;
import com.prolifera.app.Fragments.EtapaMainFragment;
import com.prolifera.app.Fragments.EtapaMetricsFragment;
import com.prolifera.app.Fragments.SampleHistoryFragment;
import com.prolifera.app.Fragments.SampleMainFragment;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.AmostraPai;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.TabAdapter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.prolifera.app.Activities.GenMainActivity.READ_CODE_REQUEST;

public class ItemDataActivity extends AppCompatActivity {

    private RequestQueue rq;
    private TextView tvItemNameItemData, tvEtapaItemData, tvUserLoggedItemData;
    private ListView lstMeasurement, lstParentItemData;
    private ViewPager vpItemData;
    private TabLayout tabItemData;
    public AmostraDTO amostra;
    public Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        tvItemNameItemData = findViewById(R.id.tvItemNameItemData);
        tvEtapaItemData = findViewById(R.id.tvEtapaItemData);
        tvUserLoggedItemData = findViewById(R.id.tvUserLoggedItemData);
        lstParentItemData = findViewById(R.id.lstParentItemData);

        usuario = (Usuario)getIntent().getExtras().get("usuario");
        amostra = (AmostraDTO)getIntent().getExtras().get("amostra");

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new SampleMainFragment(), "Dados");
        tabAdapter.add(new AddMeasureFragment(), "Medir");
        //tabAdapter.add(new AddClassificationFragment(), "Classificar");
        tabAdapter.add(new SampleHistoryFragment(), "Histórico");

        vpItemData = findViewById(R.id.vpItemData);
        vpItemData.setAdapter(tabAdapter);

        tabItemData = findViewById(R.id.tabItemData);
        tabItemData.setupWithViewPager(vpItemData);

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvItemNameItemData.setText("Amostra: " + amostra.getNome());
        tvEtapaItemData.setText(amostra.getEtapa().getCodigo()+ " - "+amostra.getEtapa().getNome());
        tvUserLoggedItemData.setText("Logado como: "+usuario.getNome());




        //updateMetricsList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == READ_CODE_REQUEST && resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null ) {
                final String id = result.getContents();
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Adicionar Parentesco")
                        .setMessage("Deseja adicionar a amostrade id"+id+" como pai da amostra atual?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final AmostraPai ap = new AmostraPai();
                                try {
                                    ap.setIdFilho(amostra.getIdAmostra());
                                    ap.setIdPai(Long.parseLong(id));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Problema na leitura de QR Code", Toast.LENGTH_SHORT);
                                }
                                String url = getResources().getString(R.string.server_address) + "amostra_pai";
                                JsonObjectRequest newParentRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        AmostraPai amostraPai = new AmostraPai();
                                        if (response != null) {
                                            amostraPai = JsonParser.parseAP(response);
                                            if (amostraPai != null) {
                                                Toast.makeText(getApplicationContext(), "Pai adicionado!", Toast.LENGTH_SHORT);
                                                List<Long> pais = amostra.getIdPais();
                                                pais.add(amostraPai.getIdPai());
                                                amostra.setIdPais(pais);
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "Erro ao enviar dados: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }){
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8";
                                    }

                                    @Override
                                    public byte[] getBody()  {
                                        return ap.fillPayload().getBytes();
                                    }
                                };
                                rq.add(newParentRequest);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Não", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }


    public void attachFragments() {
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new SampleMainFragment(), "Dados");
        tabAdapter.add(new AddMeasureFragment(), "Medir");
        //tabAdapter.add(new AddClassificationFragment(), "Classificar");
        tabAdapter.add(new SampleHistoryFragment(), "Histórico");

        vpItemData.setAdapter(tabAdapter);
        tabItemData.setupWithViewPager(vpItemData);
    }

//    private void updateMetricsList() {
//        final ArrayAdapter<String> metricsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        lstMeasurement.setAdapter(metricsAdapter);
//        for (AmostraQuantificadorDTO am : amostra.getMedidas())
//            metricsAdapter.add(am.getTexto());
//        for (AmostraQualificadorDTO ac : amostra.getClassificacoes())
//            metricsAdapter.add(ac.getTexto());
//    }

}
