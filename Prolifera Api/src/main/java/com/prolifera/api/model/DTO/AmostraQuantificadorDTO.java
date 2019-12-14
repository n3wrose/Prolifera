package com.prolifera.api.model.DTO;


import com.prolifera.api.model.DB.AmostraQuantificador;
import com.prolifera.api.model.DB.Quantificador;

import java.util.Date;

public class AmostraQuantificadorDTO {

    private Quantificador quantificador;
    private double valor;
    private Date timestamp;

    public String getTexto() {
        return quantificador.getNome()+": "+valor+" "+ quantificador.getUnidade();
    }


    public AmostraQuantificadorDTO(AmostraQuantificador am)
    {
        valor = am.getValor();
        timestamp = am.getTimestamp();
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




}
