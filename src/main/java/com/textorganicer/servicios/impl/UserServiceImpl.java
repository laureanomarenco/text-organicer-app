package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.excepciones.SessionException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDTO> getAll() throws ErrorProcessException {
        try {
            return repository.
                    findAll()
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserDTO findById(Integer id) throws ErrorProcessException {
        User userById = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));
        try {
            return mapper.entityToDto(userById);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserDTO findByUsername(String username) throws ErrorProcessException {
        User userByUsername = repository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese username"));
        try {
            return mapper.entityToDto(userByUsername);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public boolean userExists(String username) throws ErrorProcessException {
        try {
            return this.repository.findByUsername(username).isPresent();
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserDTO save(User user) throws ErrorProcessException {
        if(userExists(user.getUsername())) throw new NotFoundException("El usuario ya existe");
        try {
            String token = TokenGenerator.generateToken();
            user.setToken(token);
            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            user.setTokenExpiration(expirationDate);

            log.info("postUser - " + user);
            return mapper.entityToDto(repository.save(user));
        } catch (RuntimeException ex) {
            log.error("postUser - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    public UserDTO update(Integer id, User user) throws ErrorProcessException {
        User userToUpdate = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        try {
            user.setUserPrivate(userToUpdate.getUserPrivate());
            user.setRoles(userToUpdate.getRoles());
            user.setFolders(userToUpdate.getFolders());
            user.setToken(userToUpdate.getToken());
            user.setTokenExpiration(userToUpdate.getTokenExpiration());

            log.info("updateUser - " + user);
            return mapper.entityToDto(repository.save(user));
        } catch (RuntimeException ex) {
            log.error("updateUser - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ErrorProcessException {
        User userToDelete = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        try {
            repository.delete(userToDelete);
            log.info("deletedUser - " + id);
            return true;
        } catch (RuntimeException ex) {
            log.error("deleteUser - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserDTO findByToken(String token) throws ErrorProcessException {
        User userToValidate = repository
                .findByToken(token)
                .orElseThrow(() -> new SessionException("Token inexistente"));

        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(userToValidate.getTokenExpiration())) throw new SessionException("Token expirado");

        try {
            return mapper.entityToDto(userToValidate);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }
}