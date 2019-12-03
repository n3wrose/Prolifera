package com.prolifera.api.model;

public class Vertice {
    public long idPai;
    public long idFilho;

    public Vertice(long idPai, long idFilho ) {
        this.idFilho = idFilho;
        this.idPai = idPai;
    }
}
