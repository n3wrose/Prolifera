package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Qualificador;
import com.prolifera.api.model.DB.Quantificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QualificadorRepository extends JpaRepository<Qualificador, Long> {

    public List<Qualificador> findByIdEtapa(long idEtapa);
}
