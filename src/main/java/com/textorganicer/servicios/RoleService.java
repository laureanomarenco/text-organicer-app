package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dominios.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAll();
    Optional<Role> findById(Integer id);
    Role save(Role role);
    void delete(Role role);
}
