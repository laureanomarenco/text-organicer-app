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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPrivateServiceImpl implements UserPrivateService {
    private final UserPrivateRepository repository;
    private final UserPrivateMapper mapper;
    private final UserMapper userMapper;
    private final UserService userService;


    @Override
    public List<UserPrivateDTO> getAll() {
        return this.repository
                .findAll()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserPrivateDTO findById(Integer id) {
        return mapper.entityToDto(this.repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id")));
    }

    @Override
    public UserPrivateDTO save(Integer idUser, UserPrivate userPrivate) {
        if(this.exists(userPrivate.getMail()))
            throw new NotFoundException("Ya existe un usuario con ese mail");

        User user = this.userService.findById(idUser);

        byte[] salt = SaltGenerator.generateSalt();
        userPrivate.setSalt(salt);
        String hashedPassword = HashGenerator.hashPassword(userPrivate.getPassword(), salt);
        userPrivate.setPassword(hashedPassword);

        userPrivate.setUser(user);

        return mapper.entityToDto(this.repository.save(userPrivate));
    }

    @Override
    public UserPrivateDTO save(UserPrivate userPrivate, Integer idToUpdate) {
        UserPrivate userToUpdate = this.repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));

        userPrivate.setSalt(userToUpdate.getSalt());
        userPrivate.setPassword(HashGenerator.hashPassword(userPrivate.getPassword(), userToUpdate.getSalt()));
        userPrivate.setUser(userToUpdate.getUser());

        return mapper.entityToDto(this.repository.save(userPrivate));
    }

    @Override
    public UserPrivateDTO save(UserEmailUpdate userPrivate, Integer idToUpdate) {
        UserPrivate userToUpdate = this.repository
                .findById(idToUpdate)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id"));

        userToUpdate.setMail(userPrivate.getMail());
        UserPrivate updated = this.repository
                .save(userToUpdate);

        return mapper.entityToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        this.repository.delete(this.repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún usuario con ese id")));
    }

    @Override
    public UserDTO validate(UserPrivate userToValidate) {
        UserPrivate userInDB = this.findByMail(userToValidate.getMail());

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

        userValid = this.userService.save(userValid);

        return userMapper.entityToDto(userValid);
    }

    @Override
    public boolean exists(String mail) {
        return this.repository.findByMail(mail).isPresent();
    }

    public UserPrivate findByMail(String mail) {
        return this.repository
                .findByMail(mail)
                .orElseThrow(() -> new NotFoundException("Mail no encontrado"));
    }
}