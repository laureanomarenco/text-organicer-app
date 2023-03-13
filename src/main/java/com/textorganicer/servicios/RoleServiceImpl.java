package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.respositorios.RoleRepository;
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
    public Role save(Role role) {
        return this.repository.save(role);
    }

    @Override
    public void delete(Role role) {
        this.repository.delete(role);
    }
}
