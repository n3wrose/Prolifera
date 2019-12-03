package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Etapa;
import com.prolifera.api.model.DB.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessRepository extends JpaRepository<Processo, Long> {

    @Query(value = "select * from processo order by timestamp desc", nativeQuery = true)
    public List<Processo> findAllOrderByDate() ;

}
