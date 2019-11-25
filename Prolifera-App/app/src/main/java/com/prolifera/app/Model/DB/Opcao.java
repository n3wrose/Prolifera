package com.prolifera.app.Model.DB;

import java.io.Serializable;

public class Opcao implements Serializable {
    
    
    private long idRegistro;
    private long idClassificacao;
    private String valor;

    public String fillPayload() {
        return "{ \"idRegistro\": "+idRegistro+"," +
                "        \"idClassificacao\": "+idClassificacao+"," +
                "        \"valor\": \""+valor+"\" }" ;
    }

    public long getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(long idClassificacao) {
        this.idClassificacao = idClassificacao;
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
