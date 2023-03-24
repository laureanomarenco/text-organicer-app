package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dominios.Role;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAll();
    Role findById(Integer id);
    List<Role> getAllByFolderId(Integer idFolder);
    List<Role> findShared(Integer idUser);
    Role findByUserAndFolder(Integer idUser, Integer idFolder);
    Role save(Role role);
    void delete(Role role);

}
