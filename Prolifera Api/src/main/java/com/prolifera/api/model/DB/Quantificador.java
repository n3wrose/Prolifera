package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Quantificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
