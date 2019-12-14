package com.prolifera.app;

import com.prolifera.app.Model.DB.Quantificador;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.Model.DTO.QualificadorDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            pdto.setTimestamp(processoObj.getString("timestamp"));
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

    public static AmostraDTO parseAmostra(JSONObject amostraObj) {
        AmostraDTO adto = new AmostraDTO();
        try {
            adto.setDataCriacao(amostraObj.getString("dataCriacao"));
            adto.setDataFim(amostraObj.getString("dataFim"));
            adto.setNome(amostraObj.getString("nome"));
            adto.setIdAmostra(amostraObj.getLong("idAmostra"));
            adto.setEtapa(parseEtapa(amostraObj.getJSONObject("etapa")));
            adto.setNumero(amostraObj.getInt("numero"));
            adto.setDescricao(amostraObj.getString("descricao"));
            adto.setUsuario(parseUser(amostraObj.getJSONObject("usuario")));
            JSONArray medidasArray = amostraObj.getJSONArray("medidas"),
                    classificacoesArray = amostraObj.getJSONArray("classificacoes");
            List<AmostraQuantificadorDTO> medidas = new ArrayList<>();
            for (int i = 0; i<medidasArray.length(); i++)
                medidas.add(parseAmostraQuantificador(medidasArray.getJSONObject(i)));
            adto.setMedidas(medidas);
            List<AmostraQualificadorDTO> classificacoes = new ArrayList<>();
            for (int i=0; i< classificacoesArray.length(); i++)
                classificacoes.add(parseAmostraQualificador(classificacoesArray.getJSONObject(i)));
            adto.setClassificacoes(classificacoes);

        } catch (Exception e) { return null; }
        return adto;
    }

    public static AmostraQuantificadorDTO parseAmostraQuantificador(JSONObject obj) {
        AmostraQuantificadorDTO amdto = new AmostraQuantificadorDTO();
        try {
            amdto.setValor(obj.getDouble("valor"));
            amdto.setTimestamp(obj.getString("timestamp"));
            amdto.setQuantificador(parseQuantificador(obj.getJSONObject("Quantificador")));

        } catch (Exception e) { }
        return amdto;
    }

    public static Quantificador parseQuantificador(JSONObject obj) {
        Quantificador quantificador = new Quantificador();
        try {
            quantificador.setIdEtapa(obj.getLong("idEtapa"));
            quantificador.setIdQuantificador(obj.getLong("idQuantificador"));
            quantificador.setNome(obj.getString("nome"));
            quantificador.setUnidade(obj.getString("unidade"));
        } catch (Exception e) { return null; }
        return quantificador;
    }

    public static AmostraQualificadorDTO parseAmostraQualificador(JSONObject obj) {
        AmostraQualificadorDTO acdto = new AmostraQualificadorDTO();
        try {
            acdto.setTimestamp(obj.getString("timestamp"));
            acdto.setValor(obj.getString("valor"));
            acdto.setQualificadorDTO(parseQualificador(obj.getJSONObject("QualificadorDTO")));
        }catch (Exception e) { return null; }
        return acdto;
    }

    public static QualificadorDTO parseQualificador(JSONObject obj) {
        QualificadorDTO cdto = new QualificadorDTO();
        try {
            cdto.setAberto(obj.getBoolean("aberto"));
            cdto.setIdQualificador(obj.getLong("idQualificador"));
            cdto.setNome(obj.getString("nome"));
            List<Opcao> opcoes = new ArrayList<>();
            JSONArray opcoesArray = obj.getJSONArray("opcoes");
            for (int i=0; i<opcoesArray.length(); i++)
                opcoes.add(parseOpcao(opcoesArray.getJSONObject(i)));
            cdto.setOpcoes(opcoes);
        }catch (Exception e) { return null; }
        return cdto;
    }

    public static Opcao parseOpcao(JSONObject obj) {
        Opcao op = new Opcao();
        try {
            op.setIdQualificador(obj.getLong("idQualificador"));
            op.setIdRegistro(obj.getLong("idRegistro"));
            op.setValor(obj.getString("valor"));
        }catch (Exception e) { return null; }
        return op;
    }
}
