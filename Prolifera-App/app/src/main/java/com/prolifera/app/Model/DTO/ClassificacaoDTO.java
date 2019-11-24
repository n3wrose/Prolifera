package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.Classificacao;
import com.prolifera.app.Model.DB.Opcao;

import java.util.List;

public class ClassificacaoDTO {

    private long idClassificacao;
    private String nome;
    private boolean aberto;
    private List<Opcao> opcoes;

    public ClassificacaoDTO(Classificacao classificacao)
    {
        idClassificacao = classificacao.getIdClassificacao();
        nome = classificacao.getNome();
        aberto = classificacao.isAberto();
    }

    public ClassificacaoDTO() { }

    public long getIdClassificacao() {
        return idClassificacao;
    }

    public void setIdClassificacao(long idClassificacao) {
        this.idClassificacao = idClassificacao;
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
