package com.prolifera.api.model;

import com.prolifera.api.model.DB.Amostra;

public class SampleBatch {

    private Amostra amostra;
    private int sample, subsample;

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
}
