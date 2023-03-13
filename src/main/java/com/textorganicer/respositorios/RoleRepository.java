package com.textorganicer.respositorios;

import com.textorganicer.negocio.dominios.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
