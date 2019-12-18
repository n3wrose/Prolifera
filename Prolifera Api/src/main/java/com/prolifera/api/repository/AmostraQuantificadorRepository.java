package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraQualificador;
import com.prolifera.api.model.DB.AmostraQuantificador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AmostraQuantificadorRepository extends JpaRepository<AmostraQuantificador, Long> {

    @Query(value = "select * from amostra_quantificador where id_amostra = :idamostra order by timestamp desc", nativeQuery = true)
    List<AmostraQuantificador> findByIdAmostra(@Param("idamostra") long idAmostra);
}
