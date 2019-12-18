package com.prolifera.app.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.AmostraQualificador;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddClassificationFragment extends Fragment {


    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_classification, container, false);
        
        /*
        
        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        amostra = ((ItemDataActivity)getActivity()).amostra;
*/


        /*rgOpcao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                btnAddQualifier.setEnabled(true);
                Log.i("other","i = "+i);
                Log.i("other","size = "+qualificadores.get(spnQualifier.getSelectedItemPosition()).getOpcoes().size());
                if (i == qualificadores.get(spnQualifier.getSelectedItemPosition()).getOpcoes().size())
                    etQualifierOther.setVisibility(View.VISIBLE);
                else
                    etQualifierOther.setVisibility(View.INVISIBLE);
            }
        });*/

        return view;
    }

}
