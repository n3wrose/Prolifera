package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(AmostraPaiID.class)
public class AmostraPai {
    @Id
    private long idFilho;
    @Id
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
