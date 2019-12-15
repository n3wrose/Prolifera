package com.prolifera.app.Model.DTO;


import com.prolifera.app.Model.DB.Qualificador;
import com.prolifera.app.Model.DB.Opcao;

import java.io.Serializable;
import java.util.List;

public class QualificadorDTO implements Serializable {

    private long idQualificador;
    private long idEtapa;
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

    public String fillPayload() {
        String retorno = " { \"idQualificador\": "+idQualificador+", \"nome\": \""+nome+"\", \"aberto\": "+aberto+", " +
                "\"idEtapa\": "+idEtapa+", \"opcoes\": [ " ;

        for (Opcao op : opcoes) {
            retorno = retorno +" "+ op.fillPayload() + "," ;

        }
        retorno = retorno.substring(0,retorno.length()-1) +  "] }";

        return retorno;
    }

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

    public long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(long idEtapa) {
        this.idEtapa = idEtapa;
    }
}
