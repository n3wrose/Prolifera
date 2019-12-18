package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.Etapa;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class EtapaDTO implements Serializable {

    public static final int STATUS_EM_ESPERA = 0;
    public static final int STATUS_EM_ANDAMENTO = 1;
    public static final int STATUS_FINALIZADO = 2;

    private String descricao;
    private String nome;
    private String codigo;
    private Usuario usuario;
    private long idEtapa;
    private String equipamento;
    private int status;
    private ProcessoDTO processo;
    private String dataFim;
    private String dataInicio;
    private String dataPrevista;
    private String timestamp;

    public EtapaDTO() {

    }

    public EtapaDTO(Etapa etapa)
    {
        descricao = etapa.getDescricao();
        nome = etapa.getNome();
        idEtapa = etapa.getIdEtapa();
        codigo = etapa.getCodigo();
        equipamento = etapa.getEquipamento();
        dataFim = etapa.getDataFim();
        dataInicio = etapa.getDataInicio();
        dataPrevista = etapa.getDataPrevista();
        status = etapa.getStatus();
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(etapa.getTimestamp());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public long getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(long idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProcessoDTO getProcesso() {
        return processo;
    }

    public void setProcesso(ProcessoDTO processo) {
        this.processo = processo;
    }

    public String getDataFim() {
        return Utils.toUserDate(dataFim);
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getDataInicio()
    {
        return Utils.toUserDate(dataInicio);
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataPrevista() {
        return Utils.toUserDate(dataPrevista);
    }

    public void setDataPrevista(String dataPrevista) {
        this.dataPrevista = dataPrevista;
    }
}
