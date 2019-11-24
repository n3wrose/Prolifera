package com.prolifera.app.Model.DB;

public class Medicao {
    
    
    private long idMedicao;
    private long idEtapa;
    private String nome;
    private String unidade;

    public String geyPayload() {
        return "{" +
                " \"idMedicao\": "+idMedicao+"," +
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

    public long getIdMedicao() {
        return idMedicao;
    }

    public void setIdMedicao(long idMedicao) {
        this.idMedicao = idMedicao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
