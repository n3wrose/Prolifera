package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Opcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRegistro;
    private long idQualificador;
    private String valor;

    public String fillPayload() {
        return "{ \"idRegistro\": "+idRegistro+"," +
                "        \"idQualificador\": "+idQualificador+"," +
                "        \"valor\": \""+valor+"\" }" ;
    }

    public long getIdQualificador() {
        return idQualificador;
    }

    public void setIdQualificador(long idQualificador) {
        this.idQualificador = idQualificador;
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
