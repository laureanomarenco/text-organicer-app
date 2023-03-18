package com.textorganicer.servicios.impl;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.repository.findByUsername(username);
    }

    @Override
    public boolean userExists(String username) {
        if(this.repository.findByUsername(username).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public User save(User user) {
        return this.repository.save(user);
    }

    @Override
    public void delete(User user) {
        this.repository.delete(user);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return this.repository.findByToken(token);
    }
}
