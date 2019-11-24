package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.AmostraMedicao;
import com.prolifera.app.Model.DB.Medicao;

import java.util.Date;

public class AmostraMedicaoDTO {

    private Medicao medicao;
    private double valor;
    private Date timestamp;

    public String getTexto() {
        return medicao.getNome()+": "+valor+" "+ medicao.getUnidade();
    }


    public AmostraMedicaoDTO(AmostraMedicao am)
    {
        valor = am.getValor();
        timestamp = am.getTimestamp();
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




}
