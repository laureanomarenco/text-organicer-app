package com.textorganicer.servicios;

import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.negocio.dto.UserPrivateDTO;

import java.util.List;

public interface UserPrivateService {
    List<UserPrivateDTO> getAll();
    UserPrivateDTO findById(Integer id);
    UserPrivate findByMail(String mail);
    UserPrivateDTO save(Integer idUser, UserPrivate userPrivate);
    UserPrivateDTO save(UserPrivate userPrivate, Integer idToUpdate);
    UserPrivateDTO save(UserEmailUpdate userPrivate, Integer idToUpdate);
    boolean delete(Integer id);
    UserDTO validate(UserPrivate userToValidate);
    boolean exists(String mail);
}
