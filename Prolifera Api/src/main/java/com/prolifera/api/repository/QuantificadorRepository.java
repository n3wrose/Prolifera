package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Quantificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuantificadorRepository extends JpaRepository<Quantificador, Long> {

    public List<Quantificador> findByIdEtapa(long idEtapa);
}
