package com.prolifera.app.Model.DB;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Etapa {

    public static final int STATUS_EM_ESPERA = 0;
    public static final int STATUS_EM_ANDAMENTO = 1;
    public static final int STATUS_FINALIZADO = 2;

    private String codigo;
    private String nome;
    private Long idEtapa;
    private Long idProcesso;
    private String descricao;
    private String equipamento;
    private Date dataInicio;
    private Date dataFim;
    private Date dataPrevista;
    private int status;
    private Date timestamp;
    private String usuario;

    public String fillPayload() {
        return "{\n" +
                "        \"descricao\": \""+ descricao +"\",\n" +
                "        \"nome\": \""+ nome +"\",\n" +
                "        \"codigo\": \""+ codigo +"\",\n" +
                "        \"usuario\": \""+ usuario +"\",\n" +
                "        \"idEtapa\": "+idEtapa+",\n" +
                "        \"equipamento\": "+ equipamento +",\n" +
                "        \"status\": "+ status +",\n" +
                "        \"idProcesso\": "+ idProcesso +",\n" +
                "        \"dataFim\": \""+ getDataFim() +"\",\n" +
                "        \"dataInicio\": \""+ getDataInicio() +"\",\n" +
                "        \"dataPrevista\": \""+ getDataPrevista() +"\"\n" +
                "    }";
    }

    public String getDataFim() {
        return dataFim == null ? null :new SimpleDateFormat("yyyy-MM-dd").format(dataFim);
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getDataPrevista() {
        return dataPrevista == null ? null :new SimpleDateFormat("yyyy-MM-dd").format(dataPrevista);
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataInicio() {
        return dataInicio == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(dataInicio);
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(Long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
