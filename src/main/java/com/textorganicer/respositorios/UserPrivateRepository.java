package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.UserPrivate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPrivateRepository extends JpaRepository<UserPrivate, Integer> {
    Optional<UserPrivate> findByMail(String mail);
}