package com.prolifera.app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prolifera.R;

import androidx.fragment.app.Fragment;

public class QuantifierFragment extends Fragment {

    private TextView tvEnterQuantifierName, tvNewMetricsQuantifier,tvEnterQuantifierUnity;
    private Button  btnCreateQuantifier;
    private EditText etQuantifierUnity, etQuantifierName;

    public QuantifierFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_quantifier, container, false);

        tvNewMetricsQuantifier = view.findViewById(R.id.tvNewMetricsQuantifier);
        etQuantifierName = view.findViewById(R.id.etQuantifierName);
        etQuantifierUnity = view.findViewById(R.id.etQuantifierUnity);

        btnCreateQuantifier = view.findViewById(R.id.btnCreateQuantifier);
        tvEnterQuantifierName = view.findViewById(R.id.tvEnterQuantifierName);
        tvEnterQuantifierUnity = view.findViewById(R.id.tvEnterQuantifierUnity);

        return view;
    }
}
