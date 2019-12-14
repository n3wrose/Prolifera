package com.prolifera.app.Model.DB;

import java.util.Date;


public class AmostraQualificador {

    
    
    private long idRegistro;
    private long idAmostra;
    private long idQualificador;
    private Date timestamp;
    private String valor;

    public String fillPayload() {
        return "{ \"idAmostra\": "+idAmostra+", \"idQualificador\": "+idQualificador+", \"valor\":" +
                " \""+valor+"\", \"idRegistro\": "+idRegistro+" }" ;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getIdAmostra() {
        return idAmostra;
    }

    public void setIdAmostra(long idAmostra) {
        this.idAmostra = idAmostra;
    }

    public long getIdQualificador() {
        return idQualificador;
    }

    public void setIdQualificador(long idClassificador) {
        this.idQualificador = idClassificador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(long idRegistro) {
        this.idRegistro = idRegistro;
    }
}
