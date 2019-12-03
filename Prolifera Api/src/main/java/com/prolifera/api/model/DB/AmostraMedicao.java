package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class  AmostraMedicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
