package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.AmostraMedicao;
import com.prolifera.app.Model.DB.Medicao;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class AmostraMedicaoDTO implements Serializable {

    private Medicao medicao;
    private double valor;
    private String timestamp;

    public String getTexto() {
        return medicao.getNome()+": "+valor+" "+ medicao.getUnidade();
    }


    public AmostraMedicaoDTO(AmostraMedicao am)
    {
        valor = am.getValor();
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(am.getTimestamp());
    }

    public AmostraMedicaoDTO() { }

    public Medicao getMedicao() {
        return medicao;
    }

    public void setMedicao(Medicao medicao) {
        this.medicao = medicao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }




}
