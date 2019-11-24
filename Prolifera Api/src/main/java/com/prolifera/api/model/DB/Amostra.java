package com.prolifera.api.model.DB;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Amostra {
    private String nome;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idAmostra;
    private long idEtapa;
    private String descricao;
    private String usuario;
    private Date dataCriacao ;
    private Date dataFim;

    public String fillPayload() {
        return "{ \"dataCriacao\": \""+getDataCriacao()+"\", \"dataFim\": \""+getDataFim()+"\", " +
                "\"nome\": \""+nome+"\", \"idAmostra\": "+idAmostra+", \"idEtapa\": "+idEtapa+", \"descricao\": " +
                "\""+descricao+"\", \"usuario\":  \""+usuario+"\" }" ;
    }

    public long getIdAmostra() {
        return idAmostra;
    }

    public void setIdAmostra(long idAmostra) {
        this.idAmostra = idAmostra;
    }

    public long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDataFim() {
        return dataFim == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(dataFim);
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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

    public String getDataCriacao() {
        return dataCriacao == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(dataCriacao);
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

