package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraQuantificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmostraQuantificadorRepository extends JpaRepository<AmostraQuantificador, Long> {

    List<AmostraQuantificador> findByIdAmostra(long idAmostra);
}
