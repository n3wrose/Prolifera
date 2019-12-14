package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraQualificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmostraQualificadorRepository extends JpaRepository<AmostraQualificador, Long> {

    List<AmostraQualificador> findByIdAmostra(long idAmostra);
}
