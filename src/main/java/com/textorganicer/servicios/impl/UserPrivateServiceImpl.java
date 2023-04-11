package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.AuthException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.negocio.dto.UserPrivateDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.negocio.dto.mapper.UserPrivateMapper;
import com.textorganicer.respositorios.UserPrivateRepository;
import com.textorganicer.servicios.UserPrivateService;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.HashGenerator;
import com.textorganicer.utils.SaltGenerator;
import com.textorganicer.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPrivateServiceImpl implements UserPrivateService {
    private final UserPrivateRepository repository;
    private final UserPrivateMapper mapper;
    private final UserMapper userMapper;
    private final UserService userService;


    @Override
    public List<UserPrivateDTO> getAll() {
        List<UserPrivateDTO> all;
        try {
            all = repository
                    .findAll()
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return all;
    }

    @Override
    public UserPrivateDTO findById(Integer id) {
        UserPrivateDTO byId;
        try {
            byId = mapper.entityToDto(repository
                    .findById(id)
                    .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id")));
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return byId;
    }

    @Override
    @Transactional
    public UserPrivateDTO save(Integer idUser, UserPrivate userPrivate) {
        if(exists(userPrivate.getMail()))
            throw new NotFoundException("Ya existe un usuario con ese mail");

        UserPrivateDTO saved;
        try {
            User user = userService.findById(idUser);

            byte[] salt = SaltGenerator.generateSalt();
            userPrivate.setSalt(salt);
            String hashedPassword = HashGenerator.hashPassword(userPrivate.getPassword(), salt);
            userPrivate.setPassword(hashedPassword);

            userPrivate.setUser(user);

            saved = mapper.entityToDto(repository.save(userPrivate));
        } catch (RuntimeException ex) {
            log.error("postUserPrivate - " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        log.info("postUserPrivate - " + saved.toString());
        return saved;
    }

    @Override
    @Transactional
    public UserPrivateDTO save(UserPrivate userPrivate, Integer idToUpdate) {
        UserPrivateDTO updaterUserDTO;
        try {
        UserPrivate userToUpdate = repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));

        userPrivate.setSalt(userToUpdate.getSalt());
        userPrivate.setPassword(HashGenerator.hashPassword(userPrivate.getPassword(), userToUpdate.getSalt()));
        userPrivate.setUser(userToUpdate.getUser());

        updaterUserDTO = mapper.entityToDto(repository.save(userPrivate));
        } catch (RuntimeException ex) {
            log.error("updatePrivateUser - " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        log.info("updatePrivateUser - " + updaterUserDTO.toString());
        return updaterUserDTO;
    }

    @Override
    @Transactional
    public UserPrivateDTO save(UserEmailUpdate userPrivate, Integer idToUpdate) {
        UserPrivateDTO updatedDTO;
        try {
        UserPrivate userToUpdate = repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));

        userToUpdate.setMail(userPrivate.getMail());
        updatedDTO = mapper.entityToDto(repository
                .save(userToUpdate));
        } catch (RuntimeException ex) {
            log.info("updateUserEmail - " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        log.info("updateUserEmail - " + updatedDTO.toString());
        return updatedDTO;
    }

    @Override
    public boolean delete(Integer id) {
        try {
        repository.delete(this.repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id")));
        } catch (RuntimeException ex) {
            log.info("deleteUserPrivate - " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        log.info("deleteUserPrivate - " + id);
        return true;
    }

    @Override
    public UserDTO validate(UserPrivate userToValidate) {
        UserDTO userValidDTO;
        try {
        UserPrivate userInDB = findByMail(userToValidate.getMail());

        boolean isValid = HashGenerator.verifyPassword(
                userToValidate.getPassword(),
                userInDB.getSalt(),
                userInDB.getPassword());

        if(!isValid) throw new AuthException("Password incorrecto");
        User userValid = userInDB.getUser();
        String token = TokenGenerator.generateToken();
        userValid.setToken(token);

        LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
        userValid.setTokenExpiration(expirationDate);

        userValidDTO = userMapper.entityToDto(this.userService.save(userValid));
        } catch (RuntimeException ex) {
            log.info("login - " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        log.info("login - " + userValidDTO.toString());
        return userValidDTO;
    }

    @Override
    public boolean exists(String mail) {
        boolean existsByMail;
        try {
            existsByMail = repository.findByMail(mail).isPresent();
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return existsByMail;
    }

    @Override
    public UserPrivate findByMail(String mail) {
        UserPrivate userByMail;
        try {
            userByMail = repository
                    .findByMail(mail)
                    .orElseThrow(() -> new NotFoundException("Mail no encontrado"));
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return userByMail;
    }
}