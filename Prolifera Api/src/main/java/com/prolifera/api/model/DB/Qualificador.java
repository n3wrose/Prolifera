package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Qualificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idQualificador;
    private long idEtapa;
    private String nome;
    private boolean aberto;

    public String fillPayload() {
        return " { \"idQualificador\": "+idQualificador+", \"nome\": \""+nome+"\", \"aberto\": "+aberto+", " +
                "\"idEtapa\": "+idEtapa+" }";
    }

    public long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public long getIdQualificador() {
        return idQualificador;
    }

    public void setIdQualificador(long idQualificador) {
        this.idQualificador = idQualificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
