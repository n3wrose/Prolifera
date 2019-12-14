package com.prolifera.api.model.DTO;

import com.prolifera.api.model.DB.AmostraQualificador;

import java.util.Date;

public class AmostraQualificadorDTO {
    private QualificadorDTO qualificadorDTO;
    private String valor;
    private Date timestamp;

    public AmostraQualificadorDTO(AmostraQualificador ac){
        valor = ac.getValor();
        timestamp = ac.getTimestamp();
    }

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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTexto() {
        return qualificadorDTO.getNome() + ": "+ valor;
    }
}
