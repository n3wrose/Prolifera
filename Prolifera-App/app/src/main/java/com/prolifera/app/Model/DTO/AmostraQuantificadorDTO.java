package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.AmostraQuantificador;
import com.prolifera.app.Model.DB.Quantificador;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class AmostraQuantificadorDTO implements Serializable {

    private Quantificador quantificador;
    private double valor;
    private String timestamp;

    public String getTexto() {
        return quantificador.getNome()+": "+valor+" "+ quantificador.getUnidade();
    }


    public AmostraQuantificadorDTO(AmostraQuantificador am)
    {
        valor = am.getValor();
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(am.getTimestamp());
    }

    public AmostraQuantificadorDTO() { }

    public Quantificador getQuantificador() {
        return quantificador;
    }

    public void setQuantificador(Quantificador quantificador) {
        this.quantificador = quantificador;
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
