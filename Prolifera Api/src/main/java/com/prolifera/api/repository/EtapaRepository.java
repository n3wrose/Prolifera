package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtapaRepository extends JpaRepository<Etapa, Long> {

    List<Etapa> findByIdProcesso(long idProcesso);


    @Query(value = "select * from Etapa e where e.id_processo = :idprocesso and e.status = "+Etapa.STATUS_EM_ESPERA+
            "order by e.data_prevista", nativeQuery = true)
    List<Etapa> findEmEsperaByIdProcesso(@Param("idprocesso") long idProcesso);
    @Query(value = "select * from Etapa e where e.id_processo = :idprocesso and e.status = "+Etapa.STATUS_EM_ANDAMENTO +
            " order by e.data_inicio", nativeQuery = true)
    List<Etapa> findEmAndamentoByIdProcesso(@Param("idprocesso") long idProcesso);
    @Query(value = "select * from Etapa e where e.id_processo = :idprocesso and e.status = "+Etapa.STATUS_FINALIZADO+
    " order by e.data_fim", nativeQuery = true)
    List<Etapa> findFinalizadoByIdProcesso(@Param("idprocesso") long idProcesso);
}
