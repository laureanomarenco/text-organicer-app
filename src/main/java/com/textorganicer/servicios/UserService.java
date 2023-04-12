package com.textorganicer.servicios;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll() throws ErrorProcessException;
    UserDTO findById(Integer id) throws ErrorProcessException;
    UserDTO findByUsername(String username) throws ErrorProcessException;
    boolean userExists(String username) throws ErrorProcessException;
    UserDTO save(User user) throws ErrorProcessException;
    UserDTO update(Integer id, User user) throws ErrorProcessException;
    boolean delete(Integer id) throws ErrorProcessException;
    UserDTO findByToken(String token) throws ErrorProcessException;
}