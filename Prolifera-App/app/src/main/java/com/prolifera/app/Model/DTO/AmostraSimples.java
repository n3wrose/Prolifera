package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Etapa;

import java.util.List;

public class AmostraSimples {

    private String dataCriacao;
    private String dataFim;
    private String nome;
    private long idAmostra;
    private Etapa etapa;
    private String descricao;
    private String usuario;
    private List<String> atributos;
    private List<Long> pais;
    private List<Long> filhos;

    public AmostraSimples() {}

    public AmostraSimples(Amostra amostra) {
        dataCriacao = amostra.getDataCriacao();
        nome = amostra.getNome();
        idAmostra = amostra.getIdAmostra();
        descricao = amostra.getDescricao();
        usuario = amostra.getUsuario();
        filhos = null;
    }

    public List<Long> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<Long> filhos) {
        this.filhos = filhos;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getIdAmostra() {
        return idAmostra;
    }

    public void setIdAmostra(long idAmostra) {
        this.idAmostra = idAmostra;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<String> atributos) {
        this.atributos = atributos;
    }

    public List<Long> getPais() {
        return pais;
    }

    public void setPais(List<Long> pais) {
        this.pais = pais;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
