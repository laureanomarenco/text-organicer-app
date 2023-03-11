package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    boolean userExists(String username);
    User save(User user);
    void delete(User user);
}