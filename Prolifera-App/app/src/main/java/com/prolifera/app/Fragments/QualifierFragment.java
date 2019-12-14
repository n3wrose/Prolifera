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

public class QualifierFragment extends Fragment {

    private TextView  tvNewMetricsQuantifier, tvNewMetricsQualifier,
            tvEnterQualifierOptions, tvEnterQualifierName;
    private Button btnCreateQualifier;
    private EditText  etQualifierName;

    public QualifierFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.fragment_qualifier, container, false);

		tvNewMetricsQualifier= view.findViewById(R.id.tvNewMetricsQualifier);
		etQualifierName= view.findViewById(R.id.etQualifierName);
		btnCreateQualifier = view.findViewById(R.id.btnCreateQualifier);
		tvEnterQualifierOptions = view.findViewById(R.id.tvEnterQualifierOptions);
		tvEnterQualifierName = view.findViewById(R.id.tvEnterQualifierName);
		return view;
	}
}
