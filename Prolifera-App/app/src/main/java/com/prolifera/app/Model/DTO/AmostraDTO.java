package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;

import java.io.Serializable;
import java.util.List;

public class AmostraDTO implements Serializable {

    private String dataCriacao;
    private String dataFim;
    private String nome;
    private List<Long> idPais;
    private long idAmostra;
    private EtapaDTO etapa;
    private String descricao;
    private Usuario usuario;
    private int numero;
    private List<AmostraQuantificadorDTO> medidas;
    private List<AmostraQualificadorDTO> classificacoes;
    private List<AmostraSimples> filhos;

    public AmostraDTO() {}

    public AmostraDTO(Amostra amostra) {
        dataCriacao = amostra.getDataCriacao();
        nome = amostra.getNome();
        idAmostra = amostra.getIdAmostra();
        descricao = amostra.getDescricao();
        numero = amostra.getNumero();
        dataFim = amostra.getDataFim();
    }

    public List<AmostraSimples> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<AmostraSimples> filhos) {
        this.filhos = filhos;
    }

    public List<AmostraQualificadorDTO> getClassificacoes() {
        return classificacoes;
    }

    public void setClassificacoes(List<AmostraQualificadorDTO> classificacoes) {
        this.classificacoes = classificacoes;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public List<Long> getIdPais() {
        return idPais;
    }

    public void setIdPais(List<Long> idPais) {
        this.idPais = idPais;
    }

    public List<AmostraQuantificadorDTO> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<AmostraQuantificadorDTO> medidas) {
        this.medidas = medidas;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    public EtapaDTO getEtapa() {
        return etapa;
    }

    public void setEtapa(EtapaDTO etapa) {
        this.etapa = etapa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
