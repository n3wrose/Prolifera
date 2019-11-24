package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraMedicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmostraMedicaoRepository extends JpaRepository<AmostraMedicao, Long> {

    List<AmostraMedicao> findByIdAmostra(long idAmostra);
}
