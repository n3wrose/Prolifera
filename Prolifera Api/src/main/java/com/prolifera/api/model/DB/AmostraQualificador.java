package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AmostraQualificador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
