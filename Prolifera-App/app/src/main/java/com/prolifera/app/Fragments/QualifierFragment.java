package com.prolifera.app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.CreateMetricsActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Qualificador;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QualifierFragment extends Fragment {

    private TextView  tvNewMetricsQuantifier, tvNewMetricsQualifier,
            tvEnterQualifierOptions, tvEnterQualifierName;
    private Button btnCreateQualifier;
    private EditText  etQualifierName, metOpcoes;
    private Switch swtAberto;
    private RequestQueue rq;
    private EtapaDTO etapa;
	private int a = 1;
    public QualifierFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.fragment_qualifier, container, false);

		tvNewMetricsQualifier= view.findViewById(R.id.tvNewMetricsQualifier);
		etQualifierName= view.findViewById(R.id.etQualifierName);
		swtAberto = view.findViewById(R.id.swtOpenQualifier);
		btnCreateQualifier = view.findViewById(R.id.btnCreateQualifier);
		metOpcoes = view.findViewById(R.id.metOpcoes);

		rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

		etapa = ((CreateMetricsActivity)getActivity()).etapa ;

		btnCreateQualifier.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				create();
			}
		});

		return view;
	}

	public void create() {
		try {
			Toast.makeText(getContext(),"Enviando dados . .  .",Toast.LENGTH_SHORT).show();
			btnCreateQualifier.setEnabled(false);
			final QualificadorDTO qdto = new QualificadorDTO();
			qdto.setNome(etQualifierName.getText().toString());
			qdto.setAberto(swtAberto.isChecked());
			qdto.setIdEtapa(etapa.getIdEtapa());
			List<Opcao> opcoes = new ArrayList<>();
			for (String opcao : metOpcoes.getText().toString().split("\\r?\\n")) {
				Opcao op = new Opcao();
				op.setValor(opcao);
				opcoes.add(op);
				Log.i("teste",opcao);
			}
			qdto.setOpcoes(opcoes);

			String url = getResources().getString(R.string.server_address)+"qualificador" ;
			JsonObjectRequest newQuantifierRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					btnCreateQualifier.setEnabled(true);
					QualificadorDTO qdto = new QualificadorDTO();
					if (response != null) {
						qdto = JsonParser.parseQualificador(response);
						if (!qdto.equals(null)) {
							Toast.makeText(getContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
							getActivity().finish();
						}
					} else
						Toast.makeText(getContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getContext(), "Erro ao enviar dados: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
					btnCreateQualifier.setEnabled(true);
				}

			}){
				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}

				@Override
				public byte[] getBody()  {
					return qdto.fillPayload().getBytes();
				}
			};

			rq.add(newQuantifierRequest);
		}
		catch (Exception e) {
			Toast.makeText(getContext(),"Erro ao enviar dados.",Toast.LENGTH_SHORT).show();
			Log.i("error",e.getMessage());
		}
	}
}
