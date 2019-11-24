package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {
}
