package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AmostraClassificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
