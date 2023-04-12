package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.AuthException;
import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.negocio.dto.UserPrivateDTO;
import com.textorganicer.negocio.dto.mapper.UserPrivateMapper;
import com.textorganicer.respositorios.UserPrivateRepository;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.UserPrivateService;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.HashGenerator;
import com.textorganicer.utils.SaltGenerator;
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
public class UserPrivateServiceImpl implements UserPrivateService {
    private final UserPrivateRepository repository;
    private final UserPrivateMapper mapper;
    private final UserService userService;
    private final UserRepository userRepository;


    @Override
    public List<UserPrivateDTO> getAll() throws ErrorProcessException {
        try {
            return repository
                    .findAll()
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }

    }

    @Override
    public UserPrivateDTO findById(Integer id) throws ErrorProcessException {
        UserPrivate userById = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));
        try {
            return mapper.entityToDto(userById);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserPrivateDTO save(Integer idUser, UserPrivate userPrivate) throws ErrorProcessException {
        if(exists(userPrivate.getMail())) throw new NotFoundException("Ya existe un usuario con ese mail");
        User user = userRepository
                .findById(idUser)
                .orElseThrow(() -> new NotFoundException("El usuario no existe"));
        try {

            byte[] salt = SaltGenerator.generateSalt();
            userPrivate.setSalt(salt);
            String hashedPassword = HashGenerator.hashPassword(userPrivate.getPassword(), salt);
            userPrivate.setPassword(hashedPassword);

            userPrivate.setUser(user);

            UserPrivateDTO saved = mapper.entityToDto(repository.save(userPrivate));
            log.info("postUserPrivate - " + saved.toString());
            return saved;
        } catch (RuntimeException ex) {
            log.error("postUserPrivate - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserPrivateDTO update(UserPrivate userPrivate, Integer idToUpdate) throws ErrorProcessException {
        UserPrivate userToUpdate = repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));
        try {
            userPrivate.setSalt(userToUpdate.getSalt());
            userPrivate.setPassword(HashGenerator.hashPassword(userPrivate.getPassword(), userToUpdate.getSalt()));
            userPrivate.setUser(userToUpdate.getUser());

            UserPrivateDTO updaterUserDTO = mapper.entityToDto(repository.save(userPrivate));

            log.info("updatePrivateUser - " + updaterUserDTO.toString());
            return updaterUserDTO;
        } catch (RuntimeException ex) {
            log.error("updatePrivateUser - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserPrivateDTO updateEmail(UserEmailUpdate userPrivate, Integer idToUpdate) throws ErrorProcessException {
        UserPrivate userToUpdate = repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));

        try {
            userToUpdate.setMail(userPrivate.getMail());
            UserPrivateDTO updatedDTO = mapper.entityToDto(repository
                    .save(userToUpdate));

            log.info("updateUserEmail - " + updatedDTO.toString());
            return updatedDTO;

        } catch (RuntimeException ex) {
            log.error("updateUserEmail - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }

    }

    @Override
    public boolean delete(Integer id) throws ErrorProcessException {
        UserPrivate userToDelete = this.repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));
        try {
            repository.delete(userToDelete);
            log.info("deleteUserPrivate - " + id);
            return true;
        } catch (RuntimeException ex) {
            log.error("deleteUserPrivate - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserDTO validate(UserPrivate userToValidate) throws ErrorProcessException {
        UserPrivate userInDB = findByMail(userToValidate.getMail());

        boolean isValid = HashGenerator.verifyPassword(
                userToValidate.getPassword(),
                userInDB.getSalt(),
                userInDB.getPassword());

        if(!isValid) throw new AuthException("Password incorrecto");
        try {
            User userValid = userInDB.getUser();
            String token = TokenGenerator.generateToken();
            userValid.setToken(token);

            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            userValid.setTokenExpiration(expirationDate);

            UserDTO userValidDTO = userService.save(userValid);

            log.info("login - " + userValidDTO.toString());
            return userValidDTO;
        } catch (RuntimeException ex) {
            log.error("login - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }

    }

    @Override
    public boolean exists(String mail) throws ErrorProcessException {
        try {
            return repository.findByMail(mail).isPresent();
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public UserPrivate findByMail(String mail) {
        return repository
                .findByMail(mail)
                .orElseThrow(() -> new NotFoundException("Mail no encontrado"));
    }
}