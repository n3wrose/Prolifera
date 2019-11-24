package com.prolifera.api.model.DB;

import java.io.Serializable;

public class AmostraPaiID implements Serializable {
    private long idPai;
    private long idFilho;

    public AmostraPaiID() { }

    public AmostraPaiID(AmostraPai ap){
        idPai = ap.getIdPai();
        idFilho = ap.getIdFilho();
    }
}
