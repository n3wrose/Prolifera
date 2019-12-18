package com.prolifera.api.model;

import com.prolifera.api.model.DB.Amostra;

import java.util.List;

public class SampleBatch {

    private Amostra amostra;
    private int sample, subsample;
    private List<Long> idPais;

    public Amostra getAmostra() {
        return amostra;
    }

    public void setAmostra(Amostra amostra) {
        this.amostra = amostra;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public int getSubsample() {
        return subsample;
    }

    public void setSubsample(int subsample) {
        this.subsample = subsample;
    }

    public List<Long> getIdPais() {
        return idPais;
    }

    public void setIdPais(List<Long> idPais) {
        this.idPais = idPais;
    }
}
