package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;

import java.util.List;
import java.util.Optional;

public interface UserPrivateService {
    List<UserPrivate> getAll();
    UserPrivate findById(Integer id);
    UserPrivate findByMail(String mail);
    UserPrivate save(UserPrivate userPrivate);
    void delete(UserPrivate userPrivate);
    boolean validate(UserPrivate userToValidate, UserPrivate userInDB);
    boolean exists(String mail);
}
