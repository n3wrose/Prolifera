package com.prolifera.api.repository;

import com.prolifera.api.model.DB.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Usuario, String> {


}
