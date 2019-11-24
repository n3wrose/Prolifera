package com.prolifera.api.repository;

import com.prolifera.api.model.DB.AmostraPai;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmostraPaiRepository extends JpaRepository<AmostraPai, Long> {

    List<AmostraPai> findByIdFilho(long idFilho);
    List<AmostraPai> findByIdPai(long idPai);

}
