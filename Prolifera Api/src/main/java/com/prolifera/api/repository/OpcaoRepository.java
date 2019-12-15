package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Opcao;
import com.prolifera.api.model.DB.Quantificador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcaoRepository extends JpaRepository<Opcao, Long> {
    public List<Opcao> findByIdQualificador(long idQualificador);
}
