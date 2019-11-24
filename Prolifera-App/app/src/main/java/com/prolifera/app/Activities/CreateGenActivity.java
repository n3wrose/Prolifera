package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Processo;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CreateGenActivity extends AppCompatActivity {

    private Button btnNextCreateGen;
    private TextView tvUserLoggedCreateGen, tvInfoNewGen;
    private EditText etGenName;
    private Usuario usuario;
    private Processo processo;
    private ProcessoDTO processoDTO = null;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gen);

        //component attribution
        btnNextCreateGen = findViewById(R.id.btnNextCreateGen);
        tvUserLoggedCreateGen = findViewById(R.id.tvUserLoggedCreateGen);
        tvInfoNewGen = findViewById(R.id.tvInfoNewGen);
        etGenName = findViewById(R.id.etGenName);

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //intent parameters retrieval
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvUserLoggedCreateGen.setText("Logado como: "+usuario.getNome());
        tvInfoNewGen.setVisibility(View.INVISIBLE);
    }

    public void seguinte(View view) {
        processo = new Processo();
        tvInfoNewGen.setText("Enviando dados ...");
        tvInfoNewGen.setVisibility(View.VISIBLE);
        processo.setLote(etGenName.getText().toString());
        processo.setUsuario(usuario.getLogin());
        String url = "http://" + getResources().getString(R.string.server_address) + ":8080/api/prolifera/processo";
        JsonObjectRequest newGenRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response.equals(null)) return;
                    processoDTO = JsonParser.parseProcesso(response);
                    tvInfoNewGen.setText("Dados recebidos!");
                    try { Thread.sleep(500); } catch (Exception e) { }
                    Intent intent = new Intent(CreateGenActivity.this, CreateEtapaActivity.class);
                    intent.putExtra("processo",processoDTO);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    tvInfoNewGen.setText("Erro ao enviar dados: "+error.getMessage());
                }

            }){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody()  {
               return processo.fillPayload().getBytes();
            }
        };

        rq.add(newGenRequest);


    }
}

