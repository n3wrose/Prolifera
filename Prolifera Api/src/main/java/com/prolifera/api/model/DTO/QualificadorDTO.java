package com.prolifera.api.model.DTO;


import com.prolifera.api.model.DB.Qualificador;
import com.prolifera.api.model.DB.Opcao;

import java.util.List;

public class QualificadorDTO {

    private long idQualificador;
    private String nome;
    private boolean aberto;
    private List<Opcao> opcoes;

    public QualificadorDTO(Qualificador qualificador)
    {
        idQualificador = qualificador.getIdQualificador();
        nome = qualificador.getNome();
        aberto = qualificador.isAberto();
    }

    public QualificadorDTO() { }

    public long getIdQualificador() {
        return idQualificador;
    }

    public void setIdQualificador(long idQualificador) {
        this.idQualificador = idQualificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }
}
