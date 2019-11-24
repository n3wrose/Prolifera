package com.prolifera.app.Model.DB;


public class AmostraPai {
    
    private long idFilho;
    
    private long idPai;

    public String fillPayload() {
        return "{ \"idPai\": "+idPai+", \"idFilho\": "+idFilho+" }";
    }

    public long getIdFilho() {
        return idFilho;
    }

    public void setIdFilho(long idFilho) {
        this.idFilho = idFilho;
    }

    public long getIdPai() {
        return idPai;
    }

    public void setIdPai(long idPai) {
        this.idPai = idPai;
    }
}
