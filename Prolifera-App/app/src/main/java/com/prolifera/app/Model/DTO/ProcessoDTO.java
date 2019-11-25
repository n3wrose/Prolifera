package com.prolifera.app.Model.DTO;

import com.prolifera.app.Model.DB.Processo;
import com.prolifera.app.Model.DB.Usuario;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class ProcessoDTO implements Serializable {

    private long idProcesso;
    private String timestamp;
    private String dataZero;
    private String lote;
    private Usuario usuario;

    public ProcessoDTO(Processo p) {
        this.idProcesso = p.getIdProcesso();
        this.lote = p.getLote();
        this.dataZero = p.getDataZero();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(p.getTimestamp());
    }

    public ProcessoDTO() {

    };

    public long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
