package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.respositorios.UserPrivateRepository;
import com.textorganicer.servicios.UserPrivateService;
import com.textorganicer.utils.HashGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPrivateServiceImpl implements UserPrivateService {

    private final UserPrivateRepository repository;

    public UserPrivateServiceImpl(UserPrivateRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserPrivate> getAll() {
        return this.repository.findAll();
    }

    @Override
    public UserPrivate findById(Integer id) {
        Optional<UserPrivate> userPrivate = this.repository.findById(id);
        if(userPrivate.isEmpty()) throw new NotFoundException("No hay ningún usuario con ese id");

        return userPrivate.get();
    }

    @Override
    public UserPrivate save(UserPrivate userPrivate) {
        return this.repository.save(userPrivate);
    }

    @Override
    public void delete(UserPrivate userPrivate) {
        this.repository.delete(userPrivate);
    }

    @Override
    public boolean validate(UserPrivate userToValidate, UserPrivate userInDB) {
        if(HashGenerator.verifyPassword(
                userToValidate.getPassword(),
                userInDB.getSalt(),
                userInDB.getPassword())) return true;

        else return false;
    }

    @Override
    public boolean exists(String mail) {
        if(this.repository.findByMail(mail).isPresent()) return true;
        else return false;
    }

    public UserPrivate findByMail(String mail) {
        Optional<UserPrivate> userPrivate = this.repository.findByMail(mail);
        if(userPrivate.isEmpty()) throw new NotFoundException("Mail no encontrado");
        return userPrivate.get();
    }
}
