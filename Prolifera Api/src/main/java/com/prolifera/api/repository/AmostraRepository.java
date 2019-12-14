package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Amostra;
import com.prolifera.api.model.DB.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AmostraRepository extends JpaRepository<Amostra, Long> {

    @Query(value = "select max(numero) from Amostra where id_etapa=:idetapa", nativeQuery = true)
    public int findLastSampleNumber(@Param("idetapa")long idEtapa);
}
