package com.textorganicer.servicios;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAll() throws ErrorProcessException;
    RoleDTO findById(Integer id) throws ErrorProcessException;
    List<RoleDTO> getAllByFolderId(Integer idFolder) throws ErrorProcessException;
    List<RoleDTO> findShared(Integer idUser) throws ErrorProcessException;
    RoleDTO findByUserAndFolder(Integer idUser, Integer idFolder) throws ErrorProcessException;
    RoleDTO save(Role role, Integer idUser, Integer idFolder) throws ErrorProcessException;
    RoleDTO update(Integer id, Role role) throws ErrorProcessException;
    boolean delete(Integer id) throws ErrorProcessException;

}
