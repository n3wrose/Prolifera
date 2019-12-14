package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.AmostraQualificador;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class AmostraQualificadorDTO implements Serializable {
    private QualificadorDTO qualificadorDTO;
    private String valor;
    private String timestamp;

    public AmostraQualificadorDTO(AmostraQualificador ac){
        valor = ac.getValor();
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ac.getTimestamp());
    }

    public AmostraQualificadorDTO() { }

    public QualificadorDTO getQualificadorDTO() {
        return qualificadorDTO;
    }

    public void setQualificadorDTO(QualificadorDTO qualificadorDTO) {
        this.qualificadorDTO = qualificadorDTO;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTexto() {
        return qualificadorDTO.getNome() + ": "+ valor;
    }
}
