package com.prolifera.api.model.DTO;


import com.prolifera.api.model.DB.AmostraQuantificador;
import com.prolifera.api.model.DB.Quantificador;

import java.util.Date;

public class AmostraQuantificadorDTO {

    private Quantificador quantificador;
    private double valor;
    private String timestamp;

    public String getTexto() {
        return quantificador.getNome()+": "+valor+" "+ quantificador.getUnidade();
    }


    public AmostraQuantificadorDTO(AmostraQuantificador am)
    {
        valor = am.getValor();
        timestamp = am.getTimestamp().toString();
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
