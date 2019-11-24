package com.prolifera.api.model.DTO;

import com.prolifera.api.model.DB.Processo;
import com.prolifera.api.model.DB.Usuario;


import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class ProcessoDTO {

    private long idProcesso;
    private Date timestamp;
    private String dataZero;
    private String lote;
    private Usuario usuario;

    public ProcessoDTO(Processo p) {
        this.idProcesso = p.getIdProcesso();
        this.lote = p.getLote();
        this.dataZero = p.getDataZero();
        this.timestamp = p.getTimestamp();
    }

    public ProcessoDTO() {

    };

    public long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDataZero() {
        return dataZero;
    }

    public void setDataZero(String dataZero) {
        this.dataZero = dataZero;
    }
}
