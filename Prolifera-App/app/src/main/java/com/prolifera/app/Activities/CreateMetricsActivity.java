package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.prolifera.R;
import com.google.android.material.tabs.TabLayout;
import com.prolifera.app.Fragments.QualifierFragment;
import com.prolifera.app.Fragments.QuantifierFragment;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.TabAdapter;

public class CreateMetricsActivity extends AppCompatActivity {

    private TextView tvUserLoggedCreateMetrics;
    private ViewPager vpMetrics;
    private TabLayout tabMetrics;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_metrics);

        tvUserLoggedCreateMetrics = findViewById(R.id.tvUserLoggedCreateMetrics);

        usuario = (Usuario)getIntent().getExtras().get("usuario");

        tvUserLoggedCreateMetrics.setText("Logado como: "+ usuario.getNome());

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new QuantifierFragment(), "Quantificador");
        tabAdapter.add(new QualifierFragment(), "Qualificador");

        vpMetrics = findViewById(R.id.vpMetrics);
        vpMetrics.setAdapter(tabAdapter);

        tabMetrics = findViewById(R.id.tabMetrica);
        tabMetrics.setupWithViewPager(vpMetrics);


    }
}
