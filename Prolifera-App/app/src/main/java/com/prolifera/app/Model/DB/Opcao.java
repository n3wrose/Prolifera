package com.prolifera.app.Model.DB;

import java.io.Serializable;

public class Opcao implements Serializable {
    
    
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
