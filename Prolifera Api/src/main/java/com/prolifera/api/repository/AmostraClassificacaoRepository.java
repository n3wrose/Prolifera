package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraClassificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmostraClassificacaoRepository extends JpaRepository<AmostraClassificacao, Long> {

    List<AmostraClassificacao> findByIdAmostra(long idAmostra);
}
