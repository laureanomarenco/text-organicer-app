package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.respositorios.RoleRepository;
import com.textorganicer.servicios.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Role> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Role findById(Integer id) {
        Optional<Role> role = this.repository.findById(id);
        if (!role.isPresent()) throw new NotFoundException("No hay ningún rol con ese id");

        return role.get();
    }

    @Override
    public List<Role> getAllByFolderId(Integer id_folder) {
        Optional<List<Role>> roles= this.repository.getAllByFolderId(id_folder);
        if (!roles.isPresent()) throw new NotFoundException("Esta carpeta no tiene roles");

        return roles.get();
    }

    @Override
    public List<Role> findShared(Integer idUser) {
        Optional<List<Role>> roles= this.repository.findShared(idUser);
        if (!roles.isPresent()) throw new NotFoundException("Ese usuario no tiene roles");

        return roles.get();
    }

    @Override
    public Role findByUserAndFolder(Integer idUser, Integer idFolder) {
        Optional<Role> role = this.repository.findByIdAndFolder(idUser, idFolder);
        if (!role.isPresent()) throw new NotFoundException("No hay roles");

        return role.get();
    }

    @Override
    public Role save(Role role) {
        return this.repository.save(role);
    }

    @Override
    public void delete(Role role) {
        this.repository.delete(role);
    }
}
