package com.prolifera.app.Model.DB;

import java.io.Serializable;

public class Quantificador implements Serializable {
    
    
    private long idQuantificador;
    private long idEtapa;
    private String nome;
    private String unidade;

    public String geyPayload() {
        return "{" +
                " \"idQuantificador\": "+idQuantificador+"," +
                " \"idEtapa\": "+idEtapa+"," +
                " \"nome\": \""+nome+"\"," +
                " \"unidade\": \""+unidade+"\"" +
                "}";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public long getIdQuantificador() {
        return idQuantificador;
    }

    public void setIdQuantificador(long idQuantificador) {
        this.idQuantificador = idQuantificador;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
