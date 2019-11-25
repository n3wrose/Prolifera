package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDataActivity extends AppCompatActivity {

    private RequestQueue rq;
    private Button btnDestruirAmostra;
    private TextView tvItemNameItemData, tvEtapaItemData, tvUserLoggedItemData;
    private ListView lstMeasurement, lstParentItemData;
    private AmostraDTO amostra;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        tvItemNameItemData = findViewById(R.id.tvItemNameItemData);
        tvEtapaItemData = findViewById(R.id.tvEtapaItemData);
        tvUserLoggedItemData = findViewById(R.id.tvUserLoggedItemData);
        lstMeasurement = findViewById(R.id.lstMeasurement);
        lstParentItemData = findViewById(R.id.lstParentItemData);
        btnDestruirAmostra = findViewById(R.id.btnDestruirAmostra);

        usuario = (Usuario)getIntent().getExtras().get("usuario");
        amostra = (AmostraDTO)getIntent().getExtras().get("amostra");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvItemNameItemData.setText(amostra.getNome());
        tvEtapaItemData.setText(amostra.getEtapa().getCodigo()+ " - "+amostra.getEtapa().getNome());
        tvUserLoggedItemData.setText("Logado como: "+usuario.getNome());

        Log.i("data","|"+amostra.getDataFim()+ "|");
        if (amostra.getDataFim().isEmpty()) Log.i("data", "1");
        if (amostra.getDataFim().equals(null)) Log.i("data", "2");
        if (amostra.getDataFim().equals("null"))Log.i("data", "3");
        if (amostra.getDataFim() == null ) Log.i("data", "4");
        if (amostra.getDataFim() == "null") Log.i("data", "5");

        if (!amostra.getDataFim().equals("null")){
            disableButton();
        }

    }

    private void updateParentList() {

    }

    private void updateMetricsList() {

    }

    public void destruir(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Destruir Amostra")
                .setMessage("Tem certeza que deseja destruir esta amostra?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        try {
                            final Amostra a = new Amostra();
                            a.setNome(amostra.getNome());
                            a.setUsuario(amostra.getUsuario().getLogin());
                            a.setIdEtapa(amostra.getEtapa().getIdEtapa());
                            a.setDescricao(amostra.getDescricao());
                            a.setDataCriacao(new SimpleDateFormat("yyyy-MM-dd").parse(amostra.getDataCriacao()));
                            a.setDataFim(new Date());
                            a.setIdAmostra(amostra.getIdAmostra());
                            String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/amostra";
                            JsonObjectRequest destroySampleRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response.equals(null)) return;
                                    amostra = JsonParser.parseAmostra(response);
                                    if (amostra != null) {
                                        disableButton();
                                    } else
                                        finish();

                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                    }
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
                                    Log.i("data",a.fillPayload());
                                    return a.fillPayload().getBytes();
                                }
                            };

                            rq.add(destroySampleRequest);
                        } catch (Exception e) { Log.e("error",e.getMessage());
                        }


                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    private void disableButton() {
        btnDestruirAmostra.setText("Destruida em "+amostra.getDataFim());
        btnDestruirAmostra.setEnabled(false);
        btnDestruirAmostra.setTextSize(20);
        btnDestruirAmostra.setBackgroundColor(Color.parseColor("#C35F5050"));
    }
}
