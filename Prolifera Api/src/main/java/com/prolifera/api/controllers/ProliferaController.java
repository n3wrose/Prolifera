package com.prolifera.api.controllers;

import com.prolifera.api.model.DB.*;
import com.prolifera.api.model.DTO.*;
import com.prolifera.api.model.DTO.EtapaDTO;
import com.prolifera.api.model.GrafoAmostra;
import com.prolifera.api.model.ImagePayload;
import com.prolifera.api.model.SampleBatch;
import com.prolifera.api.repository.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/prolifera")
public class ProliferaController {

    @Autowired
    UserRepository userRepository;
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public Usuario saveUser(@RequestBody Usuario usuario) {
        return userRepository.save(usuario);
    }
    @GetMapping("/user")
    public List<Usuario> listUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/user/{login}")
    public Usuario getUsuario(@PathVariable("login") String login) {
        Optional<Usuario> o = userRepository.findById(login);
        if (!o.isPresent())
            return null;
        return (Usuario)o.get();
    }


    @Autowired
    ProcessRepository processRepository;
    @PostMapping("/processo")
    @ResponseStatus(HttpStatus.OK)
    public ProcessoDTO saveProcess(@RequestBody Processo p) {
        Optional<Processo> op = processRepository.findById(p.getIdProcesso());
        if (op.isPresent()) {
            Processo old = op.get();
            BeanUtils.copyProperties(p, old);
            p = old;
        }
        p.setTimestamp(new Date());
        ProcessoDTO pdto = new ProcessoDTO(processRepository.saveAndFlush(p));
        pdto.setUsuario(userRepository.findById(p.getUsuario()).get());
        return pdto;
    }
    @GetMapping("/processo")
    public List<ProcessoDTO> listProcessos() {
        List<ProcessoDTO> processos = new ArrayList<ProcessoDTO>();
        for (Processo p : processRepository.findAllOrderByDate()) {
            ProcessoDTO pdto = new ProcessoDTO(p);
            pdto.setUsuario(userRepository.findById(p.getUsuario()).get());
            processos.add(pdto);
        }
        return processos;
    }
    @GetMapping("/processo/{id}")
    public ProcessoDTO getProcesso(@PathVariable("id") long id) {
        Optional<Processo> o = processRepository.findById(id);
        if (!o.isPresent())
            return null;
        return fillProcesso((Processo)o.get());
    }

    @Autowired
    EtapaRepository etapaRepository;
    @PostMapping("/etapa")
    @ResponseStatus(HttpStatus.OK)
    public EtapaDTO saveEtapa(@RequestBody Etapa e) {
        e.setTimestamp(new Date());
        EtapaDTO edto = fillEtapa(etapaRepository.saveAndFlush(e));
        System.out.println(e.fillPayload());
        return edto;
    }

    @GetMapping("/etapa")
    public List<EtapaDTO> listEtapas() {
        List<EtapaDTO> etapas = new ArrayList<EtapaDTO>();
        for (Etapa e : etapaRepository.findAll())
            etapas.add(fillEtapa(e));
        return etapas;
    }

    @Autowired
    AmostraRepository amostraRepository;

    @GetMapping("/amostra")
    public List<AmostraDTO> listAmostras(){
        List<AmostraDTO> amostras = new ArrayList<AmostraDTO>();
        for (Amostra a : amostraRepository.findAll())
            amostras.add(fillAmostra(a));
        return amostras;
    }

    @GetMapping("/amostra/{id}")
    public AmostraDTO getAmostra(@PathVariable("id") long id){
        AmostraDTO adto = null;
        Optional<Amostra> o = amostraRepository.findById(id);
        if (o.isPresent())
            adto = fillAmostra((Amostra)o.get());
        return adto;
    }

    @GetMapping("/amostra_simples")
    public List<AmostraSimples> listAmostrasSimples(){
        List<AmostraSimples> amostras = new ArrayList<AmostraSimples>();
        for (Amostra a : amostraRepository.findAll())
            amostras.add(fillAmostraSimples(a));
        return amostras;
    }



    @PostMapping("/amostra/picture/{id}")
    public void savePicture(@RequestBody ImagePayload image) throws IOException {

        AmostraDTO adto = fillAmostra(amostraRepository.findById(image.getId()).get());
        String path = "p"+ adto.getEtapa().getProcesso().getIdProcesso()+"\\e"+adto.getEtapa().getIdEtapa()+
                "\\a"+adto.getIdAmostra() ;
        System.out.println("mkdir "+path);
        new File(path).mkdirs();

        byte[] decodedBytes = Base64.getDecoder().decode(image.getImagem());
        FileUtils.writeByteArrayToFile(new File(path+"\\"+new Date()+".jpg"), decodedBytes);

        System.out.println("imagem: "+image.getImagem());
        System.out.println("id amostra: "+image.getId());
    }

    @PostMapping("/batch_amostra")
    public List<String> saveAmostra(@RequestBody SampleBatch sb) {
        Amostra a = sb.getAmostra();
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < sb.getSample(); i++)
            for (int j=1; j < sb.getSubsample()+1 ; j++) {
            Amostra amostra = new Amostra();
            amostra.setNome(Character.toString((char)(i+65))+j);
            amostra.setDataCriacao(new Date());
            amostra.setDescricao(a.getDescricao());
            amostra.setIdEtapa(a.getIdEtapa());
            amostra.setUsuario(a.getUsuario());
            amostra.setDataFim(null);
            amostraRepository.saveAndFlush(amostra);
            lista.add("id: " + amostra.getIdAmostra()+" - "+amostra.getNome());
        }
        return lista;
    }

    @Autowired
    MedicaoRepository medicaoRepository;

    @PostMapping("/medicao")
    public Medicao saveMedicao(@RequestBody Medicao medicao) {
        return medicaoRepository.saveAndFlush(medicao);
    }
    @GetMapping("/medicao")
    public List<Medicao> listMedicao(){
        return medicaoRepository.findAll();
    }

    @Autowired
    AmostraMedicaoRepository amRepository;

    @PostMapping("/amostra_medicao")
    public AmostraMedicao saveAmostraMedicao(@RequestBody AmostraMedicao am) {
        am.setTimestamp(new Date());
        System.out.println(am.fillPayload());
        return amRepository.saveAndFlush(am);
    }

    @Autowired
    ClassificacaoRepository classificacaoRepository;

    @PostMapping("/classificacao")
    public Classificacao saveClassificacao(@RequestBody Classificacao classificacao) {
        System.out.println(classificacao.fillPayload());
        return classificacaoRepository.saveAndFlush(classificacao);
    }
    @GetMapping("/classificacao")
    public List<ClassificacaoDTO> listClassificacao() {
        List<ClassificacaoDTO> cdtoList = new ArrayList<ClassificacaoDTO>();
        for (Classificacao c : classificacaoRepository.findAll())
            cdtoList.add(fillClassificacao(c));
        return cdtoList;
    }

    @Autowired
    OpcaoRepository opcaoRepository;

    @PostMapping("/opcao")
    public Opcao saveOpcao(@RequestBody Opcao opcao) {
        System.out.println(opcao.fillPayload());
        return opcaoRepository.saveAndFlush(opcao);
    }

    @GetMapping("/opcao")
    public List<Opcao> listOpcao() { return opcaoRepository.findAll(); }

    @Autowired
    AmostraClassificacaoRepository acRepository;

    @PostMapping("/amostra_classificacao")
    public AmostraClassificacao saveAc(@RequestBody AmostraClassificacao ac) {
        ac.setTimestamp(new Date());
        System.out.println(ac.fillPayload());
        return acRepository.saveAndFlush(ac);
    }

    @GetMapping("/amostra_classificacao")
    public List<AmostraClassificacaoDTO> listAc() {
        List<AmostraClassificacaoDTO> acdtoList = new ArrayList<AmostraClassificacaoDTO>();
        for (AmostraClassificacao ac : acRepository.findAll()) {
            AmostraClassificacaoDTO acdto = new AmostraClassificacaoDTO(ac);
            acdto.setClassificacaoDTO(fillClassificacao(classificacaoRepository.findById(ac.getIdClassificacao()).get()));
            acdtoList.add(acdto);
        }
        return acdtoList;

    }

    @Autowired
    AmostraPaiRepository amostraPaiRepository;

    @PostMapping("/amostra_pai")
    public AmostraPai saveAmostraPai(@RequestBody AmostraPai ap) {
        System.out.println(ap.fillPayload());
        return amostraPaiRepository.save(ap);
    }

    private ClassificacaoDTO fillClassificacao(Classificacao c) {
        ClassificacaoDTO cdto = new ClassificacaoDTO(c);
        List<Opcao> opcoes = new ArrayList<Opcao>();
        for (Opcao op : opcaoRepository.findAll())
            opcoes.add(op);
        cdto.setOpcoes(opcoes);
        return cdto;
    }

    private AmostraDTO fillAmostra(Amostra a) {
        AmostraDTO adto = new AmostraDTO(a);
        adto.setEtapa(fillEtapa(etapaRepository.findById(a.getIdEtapa()).get()));
        adto.setUsuario(userRepository.findById(a.getUsuario()).get());
        List<AmostraMedicaoDTO> amdtoList = new ArrayList<AmostraMedicaoDTO>();
        for (AmostraMedicao am : amRepository.findByIdAmostra(a.getIdAmostra())) {
            AmostraMedicaoDTO amdto = new AmostraMedicaoDTO(am);
            amdto.setMedicao(medicaoRepository.findById(am.getIdMedicao()).get());
           // amdto.setTexto(amdto.getMedicao().getNome()+": "+amdto.getValor()+" "+amdto.getMedicao().getUnidade());
            amdtoList.add(amdto);
        }
        List<AmostraClassificacaoDTO> acdtoList = new ArrayList<AmostraClassificacaoDTO>();
        for (AmostraClassificacao ac : acRepository.findByIdAmostra(a.getIdAmostra())) {
            AmostraClassificacaoDTO acdto = new AmostraClassificacaoDTO(ac);
            acdto.setClassificacaoDTO(fillClassificacao(classificacaoRepository.findById(ac.getIdClassificacao()).get()));
            acdtoList.add(acdto);
        }
        adto.setMedidas(amdtoList);
        adto.setClassificacoes(acdtoList);
        List<Long> idPais = new ArrayList<Long>();
        for (AmostraPai ap : amostraPaiRepository.findByIdFilho(a.getIdAmostra()))
            idPais.add(ap.getIdPai());
        adto.setIdPais(idPais);
        List<AmostraSimples> filhos = new ArrayList<AmostraSimples>();
        for (AmostraPai ap : amostraPaiRepository.findByIdPai(a.getIdAmostra())) {
            filhos.add(fillAmostraSimples(amostraRepository.findById(ap.getIdFilho()).get()));
        }
        adto.setFilhos(filhos);
        return adto;
    }

    private AmostraSimples fillAmostraSimples(Amostra a) {
        AmostraSimples asdto = new AmostraSimples(a);
        asdto.setEtapa(etapaRepository.findById(a.getIdEtapa()).get());
        List<String> atributos = new ArrayList<String>();
        for (AmostraMedicao am : amRepository.findByIdAmostra(a.getIdAmostra())) {
            AmostraMedicaoDTO amdto = new AmostraMedicaoDTO(am);
            amdto.setMedicao(medicaoRepository.findById(am.getIdMedicao()).get());
            atributos.add(amdto.getTexto());
        }
        for (AmostraClassificacao ac : acRepository.findByIdAmostra(a.getIdAmostra())) {
            AmostraClassificacaoDTO acdto = new AmostraClassificacaoDTO(ac);
            acdto.setClassificacaoDTO(fillClassificacao(classificacaoRepository.findById(ac.getIdClassificacao()).get()));
            atributos.add(acdto.getTexto());
        }
        asdto.setAtributos(atributos);
        List<Long> pais = new ArrayList<Long>();
        for (AmostraPai ap : amostraPaiRepository.findByIdFilho(a.getIdAmostra()))
            pais.add(ap.getIdPai());
        asdto.setPais(pais);
        List<Long> filhos = new ArrayList<Long>();
        for (AmostraPai ap : amostraPaiRepository.findByIdPai(a.getIdAmostra())) {
            filhos.add(ap.getIdFilho());
        }
        asdto.setFilhos(filhos);
        return asdto;
    }

    private EtapaDTO fillEtapa(Etapa e, ProcessoDTO pdto) {
        EtapaDTO edto = new EtapaDTO(e);
        edto.setUsuario(userRepository.findById(e.getUsuario()).get());
        edto.setProcesso(pdto);
        return edto;
    }

    private EtapaDTO fillEtapa(Etapa e) {
        EtapaDTO edto = new EtapaDTO(e);
        edto.setUsuario(userRepository.findById(e.getUsuario()).get());
        edto.setProcesso(fillProcesso(processRepository.findById(e.getIdProcesso()).get()));
        return edto;
    }
    private ProcessoDTO fillProcesso(Processo p) {
        ProcessoDTO pdto = new ProcessoDTO(p);
        pdto.setUsuario(userRepository.findById(p.getUsuario()).get());
        return pdto;
    }


    @GetMapping("/grafo/{id}")
    private GrafoAmostra fillGraph(@PathVariable("id") long id) {
        GrafoAmostra ga = new GrafoAmostra();
        addRecursive(ga,fillAmostraSimples(amostraRepository.findById(id).get()));
        return ga;
    }

    @GetMapping("/etapa/{id}")
    public List<EtapaDTO> getEtapasOrdenado(@PathVariable("id") long idProcesso) {
        List<EtapaDTO> etapas = new ArrayList<EtapaDTO>();
        Optional o = processRepository.findById(idProcesso);
        if (o.isPresent()) {
            ProcessoDTO pdto = fillProcesso((Processo)o.get());
            for (Etapa edto : etapaRepository.findEmAndamentoByIdProcesso(idProcesso))
                etapas.add(fillEtapa(edto,pdto));
            for (Etapa edto : etapaRepository.findEmEsperaByIdProcesso(idProcesso))
                etapas.add(fillEtapa(edto,pdto));
            for (Etapa edto : etapaRepository.findFinalizadoByIdProcesso(idProcesso))
                etapas.add(fillEtapa(edto, pdto));
        }
        return etapas;
    }

    @GetMapping("/etapa/started/{id}")
    public List<EtapaDTO> getEtapasAtivas(@PathVariable("id") long idProcesso) {
        List<EtapaDTO> etapas = new ArrayList<EtapaDTO>();
        Optional o = processRepository.findById(idProcesso);
        if (o.isPresent()) {
            ProcessoDTO pdto = fillProcesso((Processo)o.get());
            for (Etapa edto : etapaRepository.findEmAndamentoByIdProcesso(idProcesso))
                etapas.add(fillEtapa(edto,pdto));
        }
        return etapas;
    }

    private void addRecursive(GrafoAmostra ga, AmostraSimples as) {
        if (ga.isThere(as.getIdAmostra()))
            return;
        ga.add(as);
        for (long id : as.getPais())
            addRecursive(ga, fillAmostraSimples(amostraRepository.findById(id).get()));
        for (long id : as.getFilhos())
            addRecursive(ga, fillAmostraSimples(amostraRepository.findById(id).get()));
        return;
    }

}
