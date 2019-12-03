package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GenSelectionActivity extends AppCompatActivity {

    Usuario usuario;
    TextView tvUserLoggedGenSelection, tvCreateGen;
    List<ProcessoDTO> processos ;
    ListView lstGen;
    RequestQueue rq ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_selection);

        //component attribution
        lstGen = findViewById(R.id.lstGen);
        tvUserLoggedGenSelection = findViewById(R.id.tvUserLoggedGenSelection);
        tvCreateGen = findViewById(R.id.tvCreateGen);

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //intent parameters retrieval
        usuario = (Usuario)getIntent().getSerializableExtra("usuario");

        tvUserLoggedGenSelection.setText("Bem vindo, "+usuario.getNome()+ " "+usuario.getSobrenome());
        processos = new ArrayList<>();

        //fill listView
       updateProcesses();
    }

    public void novoGen(View view) {
            Intent intent = new Intent(this, CreateGenActivity.class);
            intent.putExtra("usuario",usuario);
            startActivity(intent);
            updateProcesses();
    }

    private void updateProcesses() {
        final ArrayAdapter<String> processosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstGen.setAdapter(processosAdapter);
        lstGen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GenSelectionActivity.this,GenMainActivity.class);
                intent.putExtra("processo",processos.get(i));
                intent.putExtra("usuario",usuario);
                startActivity(intent);
                updateProcesses();
            }
        });
        //setting up ArrayRequest
        String url = getResources().getString(R.string.server_address) + "processo";
        JsonArrayRequest processosRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.equals(null)) return;
                        int i;
                        for (i = 0; i<response.length(); i++) {
                            try {
                                ProcessoDTO pdto = JsonParser.parseProcesso(response.getJSONObject(i));
                                processos.add(pdto);
                                processosAdapter.add(pdto.getLote()+ " iniciado em "+ Utils.toUserDate(pdto.getTimestamp()));
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        rq.add(processosRequest);
    }
}
