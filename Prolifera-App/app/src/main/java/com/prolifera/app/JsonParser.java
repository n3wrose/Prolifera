package com.prolifera.app;

import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class JsonParser {

    public static Usuario parseUser(JSONObject userObj) {
        Usuario u = null;
        try {
            u = new Usuario();
            u.setNome(userObj.getString("nome"));
            u.setEmail(userObj.getString("email"));
            u.setLogin(userObj.getString("login"));
            u.setSenha(userObj.getString("senha"));
            u.setSobrenome(userObj.getString("sobrenome"));
            u.setTipo(userObj.getInt("tipo"));
        } catch (Exception e) { u = null;}
        return u;
    }

    public static ProcessoDTO parseProcesso(JSONObject processoObj) {
        ProcessoDTO pdto = new ProcessoDTO();
        try {
            pdto.setIdProcesso(processoObj.getLong("idProcesso"));
            //pdto.setTimestamp(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(processoObj.getString("timestamp")));
            pdto.setDataZero(processoObj.getString("dataZero"));
            pdto.setLote(processoObj.getString("lote"));
            pdto.setUsuario(parseUser(processoObj.getJSONObject("usuario")));
        } catch (Exception e) { pdto = null; }
        return pdto;
    }

    public static EtapaDTO parseEtapa(JSONObject etapaObj) {
        EtapaDTO edto = new EtapaDTO();
        try {
            edto.setDescricao(etapaObj.getString("descricao"));
            edto.setNome(etapaObj.getString("nome"));
            edto.setCodigo(etapaObj.getString("codigo"));
            edto.setIdEtapa(etapaObj.getLong("idEtapa"));
            edto.setEquipamento(etapaObj.getString("equipamento"));
            edto.setStatus(etapaObj.getInt("status"));
            edto.setDataFim(etapaObj.getString("dataFim"));
            edto.setDataInicio(etapaObj.getString("dataInicio"));
            edto.setDataPrevista(etapaObj.getString("dataPrevista"));
            edto.setProcesso(JsonParser.parseProcesso(etapaObj.getJSONObject("processo")));
            edto.setUsuario(JsonParser.parseUser(etapaObj.getJSONObject("usuario")));
        } catch (Exception e) { edto = null; }
        return edto;
    }
}
