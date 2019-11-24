package com.prolifera.app.Model.DB;

public class Classificacao {
    
    
    private long idClassificacao;
    private long idEtapa;
    private String nome;
    private boolean aberto;

    public String fillPayload() {
        return " { \"idClassificacao\": "+idClassificacao+", \"nome\": \""+nome+"\", \"aberto\": "+aberto+", " +
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

    public long getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(long idClassificacao) {
        this.idClassificacao = idClassificacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
