package com.prolifera.app.Model.DB;

import java.util.Date;


public class AmostraMedicao {

    
    
    private long idRegistro;
    private long idAmostra;
    private long idMedicao;
    private Date timestamp;
    private double valor;

    public String fillPayload() {
        return "{ \"idAmostra\": "+idAmostra+", \"idMedicao\": "+idMedicao+", " +
                "\"valor\": "+valor+", \"idRegistro\": "+idRegistro+" }";
    }

    public long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public long getIdAmostra() {
        return idAmostra;
    }

    public void setIdAmostra(long idAmostra) {
        this.idAmostra = idAmostra;
    }

    public long getIdMedicao() {
        return idMedicao;
    }

    public void setIdMedicao(long idMedicao) {
        this.idMedicao = idMedicao;
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
