package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Processo, Long> {
}
