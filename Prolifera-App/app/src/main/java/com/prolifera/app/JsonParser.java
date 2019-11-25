package com.prolifera.app;

import com.prolifera.app.Model.DB.Classificacao;
import com.prolifera.app.Model.DB.Medicao;
import com.prolifera.app.Model.DB.Opcao;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraClassificacaoDTO;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraMedicaoDTO;
import com.prolifera.app.Model.DTO.ClassificacaoDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.MedicaoDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
            adto.setDescricao(amostraObj.getString("descricao"));
            adto.setUsuario(parseUser(amostraObj.getJSONObject("usuario")));
            JSONArray medidasArray = amostraObj.getJSONArray("medidas"),
                    classificacoesArray = amostraObj.getJSONArray("classificacoes");
            List<AmostraMedicaoDTO> medidas = new ArrayList<>();
            for (int i = 0; i<medidasArray.length(); i++)
                medidas.add(parseAmostraMedicao(medidasArray.getJSONObject(i)));
            adto.setMedidas(medidas);
            List<AmostraClassificacaoDTO> classificacoes = new ArrayList<>();
            for (int i=0; i< classificacoesArray.length(); i++)
                classificacoes.add(parseAmostraClassificacao(classificacoesArray.getJSONObject(i)));
            adto.setClassificacoes(classificacoes);

        } catch (Exception e) { return null; }
        return adto;
    }

    public static AmostraMedicaoDTO parseAmostraMedicao(JSONObject obj) {
        AmostraMedicaoDTO amdto = new AmostraMedicaoDTO();
        try {
            amdto.setValor(obj.getDouble("valor"));
            amdto.setTimestamp(obj.getString("timestamp"));
            amdto.setMedicao(parseMedicao(obj.getJSONObject("medicao")));

        } catch (Exception e) { }
        return amdto;
    }

    public static Medicao parseMedicao(JSONObject obj) {
        Medicao medicao = new Medicao();
        try {
            medicao.setIdEtapa(obj.getLong("idEtapa"));
            medicao.setIdMedicao(obj.getLong("idMedicao"));
            medicao.setNome(obj.getString("nome"));
            medicao.setUnidade(obj.getString("unidade"));
        } catch (Exception e) { return null; }
        return medicao;
    }

    public static AmostraClassificacaoDTO parseAmostraClassificacao(JSONObject obj) {
        AmostraClassificacaoDTO acdto = new AmostraClassificacaoDTO();
        try {
            acdto.setTimestamp(obj.getString("timestamp"));
            acdto.setValor(obj.getString("valor"));
            acdto.setClassificacaoDTO(parseClassificacao(obj.getJSONObject("classificacaoDTO")));
        }catch (Exception e) { return null; }
        return acdto;
    }

    public static ClassificacaoDTO parseClassificacao(JSONObject obj) {
        ClassificacaoDTO cdto = new ClassificacaoDTO();
        try {
            cdto.setAberto(obj.getBoolean("aberto"));
            cdto.setIdClassificacao(obj.getLong("idClassificacao"));
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
            op.setIdClassificacao(obj.getLong("idClassificacao"));
            op.setIdRegistro(obj.getLong("idRegistro"));
            op.setValor(obj.getString("valor"));
        }catch (Exception e) { return null; }
        return op;
    }
}
