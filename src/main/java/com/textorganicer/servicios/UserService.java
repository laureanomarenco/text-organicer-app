package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User findById(Integer id);
    User findByUsername(String username);
    boolean userExists(String username);
    User save(User user);
    void delete(User user);
    User findByToken(String token);
}