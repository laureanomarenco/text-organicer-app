package com.textorganicer.servicios.impl;

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
    public Optional<Role> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<List<Role>> getAllByFolderId(Integer id_folder) {
        return this.repository.getAllByFolderId(id_folder);
    }

    @Override
    public Optional<List<Role>> findShared(Integer idUser) {
        return this.repository.findShared(idUser);
    }

    @Override
    public Optional<Role> findByUserAndFolder(Integer idUser, Integer idFolder) {
        return this.repository.findByIdAndFolder(idUser, idFolder);
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
