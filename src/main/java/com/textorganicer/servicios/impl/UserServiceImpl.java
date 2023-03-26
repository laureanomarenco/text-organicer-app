package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.excepciones.SessionException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public User findById(Integer id) {
        Optional<User> user = this.repository.findById(id);
        if(user.isEmpty()) throw new NotFoundException("No hay ningún usuario con ese id");

        return user.get();
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user= this.repository.findByUsername(username);
        if(user.isEmpty()) throw new NotFoundException("No hay ningún usuario con ese username");

        return user.get();
    }

    @Override
    public boolean userExists(String username) {
        return this.repository.findByUsername(username).isPresent();
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
    public User findByToken(String token) {
        Optional<User> userToValidate = this.repository.findByToken(token);
        if(userToValidate.isEmpty()) throw new SessionException("Token inexistente");
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(userToValidate.get().getTokenExpiration())) throw new SessionException("Token expirado");

        return userToValidate.get();
    }
}
