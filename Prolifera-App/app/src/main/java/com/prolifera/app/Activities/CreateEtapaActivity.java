package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEtapaActivity extends AppCompatActivity {

    private EditText etEtapaDate, etEtapaName, etEtapaCode, etEtapaDescription,etEtapaEquipament ;
    private Button btnNovaEtapa;
    private Switch swtEquipament, swtAutoStartEtapa;
    private ProcessoDTO processo;
    private Usuario usuario;
    private TextView tvUserLoggedCreateEtapa, tvEnterEtapaEquipament;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_etapa);

        //component attribution
        tvUserLoggedCreateEtapa = findViewById(R.id.tvUserLoggedCreateEtapa);
        etEtapaDate = findViewById(R.id.etEtapaDate);
        etEtapaCode = findViewById(R.id.etEtapaCode);
        etEtapaName = findViewById(R.id.etEtapaName);
        etEtapaDescription = findViewById(R.id.etEtapaDescription);
        swtAutoStartEtapa = findViewById(R.id.swtAutoStartEtapa);
        etEtapaEquipament = findViewById(R.id.etEtapaEquipament);
        swtEquipament = findViewById(R.id.swtEquipament);
        tvEnterEtapaEquipament = findViewById(R.id.tvEnterEtapaEquipament);
        btnNovaEtapa = findViewById(R.id.btnNextCreateEtapa);

        //intent parameters retrieval
        processo = (ProcessoDTO) getIntent().getExtras().get("processo");
        usuario = (Usuario) getIntent().getExtras().get("usuario");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvUserLoggedCreateEtapa.setText("Logado como: " + usuario.getNome());

        etEtapaDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              /*
            }
        });
        etEtapaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {*/
                if (etEtapaDate.isFocused())
                    new DatePickerDialog(CreateEtapaActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        etEtapaDate.setText(sdf.format(myCalendar.getTime()));
    }



    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public void createEtapa(View view) throws ParseException {

        btnNovaEtapa.setEnabled(false);
        final Etapa etapa = new Etapa();
        etapa.setIdProcesso(processo.getIdProcesso());
        etapa.setCodigo(etEtapaCode.getText().toString());
        etapa.setNome(etEtapaName.getText().toString());
        etapa.setDescricao(etEtapaDescription.getText().toString());
        if (swtEquipament.isChecked())
            etapa.setEquipamento(etEtapaEquipament.getText().toString());
        etapa.setDataPrevista(new SimpleDateFormat("dd/MM/yyyy").parse(etEtapaDate.getText().toString()));
        etapa.setUsuario(usuario.getLogin());
        if (swtAutoStartEtapa.isChecked()) {
            etapa.setStatus(Etapa.STATUS_EM_ANDAMENTO);
            etapa.setDataInicio(new SimpleDateFormat("dd/MM/yyyy").parse(etEtapaDate.getText().toString()));
        } else
            etapa.setStatus(Etapa.STATUS_EM_ESPERA);
        etapa.setDataPrevista(new Date());

        Log.i("teste",etapa.fillPayload());
        String url = getResources().getString(R.string.server_address) + "etapa";

        JsonObjectRequest newEtapaRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                btnNovaEtapa.setEnabled(true);
                EtapaDTO edto = new EtapaDTO();
                if (response != null) {
                    edto = JsonParser.parseEtapa(response);
                    Intent intent = new Intent(CreateEtapaActivity.this, EtapaMainActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("etapa",edto);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao enviar dados: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                btnNovaEtapa.setEnabled(true);
            }

        }){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody()  {
                return etapa.fillPayload().getBytes();
            }
        };

        rq.add(newEtapaRequest);
        Toast.makeText(getApplicationContext(), "Enviando dados . . .", Toast.LENGTH_SHORT).show();

    }



    public void switchEqp(View view) {

        if (swtEquipament.isChecked()) {
            tvEnterEtapaEquipament.setVisibility(View.VISIBLE);
            etEtapaEquipament.setVisibility(View.VISIBLE);
        }
        else {
            tvEnterEtapaEquipament.setVisibility(View.INVISIBLE);
            etEtapaEquipament.setVisibility(View.INVISIBLE);
        }
    }




}
