package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Amostra;
import com.prolifera.api.model.DB.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmostraRepository extends JpaRepository<Amostra, Long> {

    @Query(value = "select max(numero) from Amostra where id_etapa=:idetapa", nativeQuery = true)
    public Integer findLastSampleNumber(@Param("idetapa")long idEtapa);

    @Query(value = "select * from Amostra where id_etapa=:idetapa order by id_amostra", nativeQuery = true)
    public List<Amostra> findAllByIdEtapa(@Param("idetapa") long idEtapa);

    @Query(value = "select count(*) from amostra where id_etapa in (select id_etapa from etapa where id_processo = :idprocesso)",
    nativeQuery = true)
    public Integer countSamplesByIdProcesso(@Param("idprocesso") long idProcesso);


}
