package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role findById(Integer id);
    List<Role> getAllByFolderId(Integer idFolder);
    List<Role> findShared(Integer idUser);
    Role findByUserAndFolder(Integer idUser, Integer idFolder);
    Role save(Role role);
    void delete(Role role);

}
