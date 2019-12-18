package com.prolifera.app.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.AmostraQualificador;
import com.prolifera.app.Model.DB.AmostraQuantificador;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddMeasureFragment extends Fragment {

    private List<Quantificador> quantificadores;
    private RequestQueue rq;
    private TextView tvQuantificatorValue, tvQuantificatorUnity;
    private EditText etQuantifierValue ;
    private Spinner spnQuantificator;
    private Button btnAddQuantifier;
    private AmostraDTO amostra;


    private boolean isOther = false;
    private String selectedText = "";
    private RadioGroup rgOpcao;
    private EditText etQualifierOther ;
    private Button btnAddQualifier;
    private Spinner spnQualifier;
    private List<QualificadorDTO> qualificadores;
    private List<RadioButton> radios ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_measure, container, false);

        btnAddQuantifier = view.findViewById(R.id.btnAddQuantifier);
        spnQuantificator = view.findViewById(R.id.spnQuantificator);
        etQuantifierValue = view.findViewById(R.id.etQuantificatorValue);
        tvQuantificatorUnity = view.findViewById(R.id.tvQuantificatorUnity);


        rgOpcao = view.findViewById(R.id.rgOpcao);
        btnAddQualifier = view.findViewById(R.id.btnAddQualifier);
        spnQualifier = view.findViewById(R.id.spnQualificator);
        etQualifierOther = view.findViewById(R.id.etQualificatorOther);

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        amostra = ((ItemDataActivity)getActivity()).amostra;

        fillQuantifierSpinner();

        btnAddQuantifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuantifier();
            }
        });


        fillQualifierComponents();
        etQualifierOther.setVisibility(View.INVISIBLE);

        btnAddQualifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQualifier();
            }
        });

        if (!amostra.getDataFim().equals("null")) {
            btnAddQuantifier.setEnabled(false);
            btnAddQuantifier.setBackgroundColor(Color.parseColor("#C35F5050"));
            btnAddQualifier.setEnabled(false);
            btnAddQualifier.setBackgroundColor(Color.parseColor("#C35F5050"));
        }

        return view;
    }


    private void addQuantifier() {
        btnAddQuantifier.setEnabled(false);
        final AmostraQuantificador aq = new AmostraQuantificador();
        Toast.makeText(getContext(), "Enviando dados . . . ", Toast.LENGTH_SHORT).show();
        aq.setIdAmostra(amostra.getIdAmostra());
        aq.setValor(Double.parseDouble(etQuantifierValue.getText().toString()));
        aq.setIdQuantificador(quantificadores.get(spnQuantificator.getSelectedItemPosition()).getIdQuantificador());

        String url =  getResources().getString(R.string.server_address) + "amostra_quantificador";
        JsonObjectRequest aqtRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response.equals(null)) return;
                AmostraQuantificadorDTO aqdto = JsonParser.parseAmostraQuantificador(response);
                if (aqdto.equals(null))
                    Toast.makeText(getContext(),"Erro ao enviar dados, tente novamente.",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    etQuantifierValue.setText("");
                    List<AmostraQuantificadorDTO> aqdtos = amostra.getMedidas();
                    aqdtos.add(aqdto);
                    amostra.setMedidas(aqdtos);
                    ((ItemDataActivity)getActivity()).amostra = amostra;
                    ((ItemDataActivity)getActivity()).attachFragments();

                }
                btnAddQuantifier.setEnabled(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Erro de conexão, favor ver log.",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return aq.fillPayload().getBytes();
            }
        };
        rq.add(aqtRequest);
    }

    private void fillQuantifierSpinner() {
        quantificadores = new ArrayList<>();
        final ArrayAdapter<String> qtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);

        spnQuantificator.setAdapter(qtAdapter);

        //setting up ArrayRequest
        String url = getResources().getString(R.string.server_address) + "quantificador/"+amostra.getEtapa().getIdEtapa();
        JsonArrayRequest qtRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.equals(null))
                            for (int i = 0; i<response.length(); i++) {
                                try {
                                    Quantificador q = JsonParser.parseQuantificador(response.getJSONObject(i));
                                    quantificadores.add(q);
                                    qtAdapter.add(q.getNome());
                                    tvQuantificatorUnity.setText(q.getUnidade());
                                } catch (Exception e) { e.printStackTrace(); }
                            }
                        if (quantificadores.size() == 0) {
                            qtAdapter.add("Não há quantificadores!");
                            btnAddQuantifier.setBackgroundColor(Color.parseColor("#C35F5050"));
                            btnAddQuantifier.setEnabled(false);
                        }
                        else
                            btnAddQuantifier.setEnabled(true);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Houve um erro de conexão, tente novamente.",Toast.LENGTH_SHORT);
                        qtAdapter.add("Não há quantificadores!");
                        btnAddQuantifier.setEnabled(false);
                        btnAddQuantifier.setBackgroundColor(Color.parseColor("#C35F5050"));
                    }
                });
        rq.add(qtRequest);
    }

    private void addQualifier() {
        final AmostraQualificador aq = new AmostraQualificador();
        aq.setIdQualificador(qualificadores.get(spnQualifier.getSelectedItemPosition()).getIdQualificador());
        aq.setIdAmostra(amostra.getIdAmostra());
        if (isOther)
            aq.setValor(etQualifierOther.getText().toString());
        else
            aq.setValor(selectedText);
        /*Log.i("radio","selected: "+rgOpcao.getCheckedRadioButtonId());
        for (RadioButton rb : radios) {
            Log.i("radio",""+rb.getId());

            if (rb.getId() == rgOpcao.getCheckedRadioButtonId())
                aq.setValor(rb.getText().toString());
        }
        if(aq.getValor().equals("Outro"))
            aq.setValor(etQualifierOther.getText().toString());*/

        String url =  getResources().getString(R.string.server_address) + "amostra_qualificador";
        JsonObjectRequest aqtRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response.equals(null)) return;
                AmostraQualificadorDTO aqdto = JsonParser.parseAmostraQualificador(response);
                Log.i("response","r = "+response.toString());
                if (aqdto == null)
                    Toast.makeText(getContext(),"Erro ao enviar dados, tente novamente.",Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    rgOpcao.clearCheck();
                    List<AmostraQualificadorDTO> aqdtos = amostra.getClassificacoes();
                    aqdtos.add(aqdto);
                    amostra.setClassificacoes(aqdtos);
                    ((ItemDataActivity)getActivity()).amostra = amostra;
                    ((ItemDataActivity)getActivity()).attachFragments();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Erro de conexão, favor ver log.",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return aq.fillPayload().getBytes();
            }
        };
        rq.add(aqtRequest);

        //aqdto.setValor();
    }

    private void fillQualifierComponents() {
        qualificadores = new ArrayList<>();
        btnAddQualifier.setEnabled(false);
        final ArrayAdapter<String> qlAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        spnQualifier.setAdapter(qlAdapter);
        spnQualifier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                populateRadioGroup(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //setting up ArrayRequest
        String url = getResources().getString(R.string.server_address) + "qualificador/"+amostra.getEtapa().getIdEtapa();
        JsonArrayRequest qlRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.equals(null))
                            for (int i = 0; i<response.length(); i++) {
                                try {
                                    QualificadorDTO q = JsonParser.parseQualificador(response.getJSONObject(i));
                                    if (q != null) {
                                        qualificadores.add(q);
                                        qlAdapter.add(q.getNome());
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        if (qualificadores.size() == 0) {
                            qlAdapter.add("Não há qualificadores!");
                            btnAddQualifier.setBackgroundColor(Color.parseColor("#C35F5050"));
                            btnAddQualifier.setEnabled(false);
                        }
                        else {
                            btnAddQualifier.setEnabled(true);
                            populateRadioGroup(0);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Houve um erro de conexão, tente novamente.",Toast.LENGTH_SHORT);
                        qlAdapter.add("Não há quantificadores!");
                        btnAddQualifier.setEnabled(false);
                    }
                });
        rq.add(qlRequest);
    }

    private void populateRadioGroup(int index) {
        rgOpcao.removeAllViews();
        radios = new ArrayList<>();
        RadioGroup rg = new RadioGroup(getContext());
        etQualifierOther.setText("");
        etQualifierOther.setVisibility(View.INVISIBLE);
        rg.setOrientation(LinearLayout.VERTICAL);
        if (qualificadores.size() > 0) {
            for (Opcao op : qualificadores.get(index).getOpcoes()) {
                final RadioButton rb = new RadioButton(getContext());
                rb.setId(View.generateViewId());
                rb.setText(op.getValor());
                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            etQualifierOther.setVisibility(View.INVISIBLE);
                            selectedText = rb.getText().toString();
                            isOther = false;
                        }
                    }
                });
                rg.addView(rb);
                radios.add(rb);
            }
            if (qualificadores.get(index).isAberto()) {
                RadioButton rb = new RadioButton(getContext());
                rb.setId(View.generateViewId());
                rb.setText("Outro");
                rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            isOther = true;
                            etQualifierOther.setVisibility(View.VISIBLE);
                        }
                    }
                });
                rg.addView(rb);
                radios.add(rb);
            }
        }
        rgOpcao.addView(rg);
    }
}
