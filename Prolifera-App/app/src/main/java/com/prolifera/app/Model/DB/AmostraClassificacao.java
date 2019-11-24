package com.prolifera.app.Model.DB;

import java.util.Date;


public class AmostraClassificacao {

    
    
    private long idRegistro;
    private long idAmostra;
    private long idClassificacao;
    private Date timestamp;
    private String valor;

    public String fillPayload() {
        return "{ \"idAmostra\": "+idAmostra+", \"idClassificacao\": "+idClassificacao+", \"valor\":" +
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

    public long getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(long idClassificador) {
        this.idClassificacao = idClassificador;
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
