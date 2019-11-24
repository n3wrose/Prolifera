package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.AmostraClassificacao;

import java.util.Date;

public class AmostraClassificacaoDTO {
    private ClassificacaoDTO classificacaoDTO;
    private String valor;
    private Date timestamp;

    public AmostraClassificacaoDTO(AmostraClassificacao ac){
        valor = ac.getValor();
        timestamp = ac.getTimestamp();
    }

    public ClassificacaoDTO getClassificacaoDTO() {
        return classificacaoDTO;
    }

    public void setClassificacaoDTO(ClassificacaoDTO classificacaoDTO) {
        this.classificacaoDTO = classificacaoDTO;
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
        return classificacaoDTO.getNome() + ": "+ valor;
    }
}
