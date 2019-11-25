package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.AmostraClassificacao;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class AmostraClassificacaoDTO implements Serializable {
    private ClassificacaoDTO classificacaoDTO;
    private String valor;
    private String timestamp;

    public AmostraClassificacaoDTO(AmostraClassificacao ac){
        valor = ac.getValor();
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ac.getTimestamp());
    }

    public AmostraClassificacaoDTO() { }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTexto() {
        return classificacaoDTO.getNome() + ": "+ valor;
    }
}
