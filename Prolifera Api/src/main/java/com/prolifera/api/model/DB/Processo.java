package com.prolifera.api.model.DB;

import com.prolifera.api.model.DTO.ProcessoDTO;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long idProcesso;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dataZero;
    private String lote;
    private String usuario;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public Processo(ProcessoDTO pdto) throws ParseException {
        this.idProcesso = pdto.getIdProcesso();
        this.dataZero = new SimpleDateFormat("yyyy-MM-dd").parse(pdto.getDataZero());
        this.lote = pdto.getLote();
        this.timestamp = pdto.getTimestamp();
        this.usuario = pdto.getUsuario().getLogin();

    }

    public Processo() {
    }

    public String fillPayload() {
        return "{" +
                "    \"idProcesso\": "+ idProcesso +"," +
                "    \"dataZero\": \""+ getDataZero() +"\"," +
                "    \"lote\": \""+ lote +"\"," +
                "    \"usuario\": \" "+usuario+"\" }";
    }

    public long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getDataZero() {
        return dataZero==null ? null : new SimpleDateFormat("yyyy-MM-dd").format(dataZero);
    }

    public void setDataZero(Date dataZero) {
        this.dataZero = dataZero;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
