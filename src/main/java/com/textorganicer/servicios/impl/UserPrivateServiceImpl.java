package com.textorganicer.servicios.impl;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.respositorios.UserPrivateRepository;
import com.textorganicer.servicios.UserPrivateService;
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
    public Optional<UserPrivate> findById(Integer id) {
        return this.repository.findById(id);
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
    public boolean validate(UserPrivate userToValidate, Optional<UserPrivate> userInDB) {
        if(userToValidate.getMail().equals(userInDB.orElseThrow().getMail())
                && userToValidate.getPassword().equals(userInDB.orElseThrow().getPassword())) return true;
        else return false;
    }

    public Optional<UserPrivate> findByMail(String mail) {
        return this.repository.findByMail(mail);
    }
}
