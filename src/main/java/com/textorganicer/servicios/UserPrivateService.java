package com.textorganicer.servicios;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.negocio.dto.UserPrivateDTO;

import java.util.List;

public interface UserPrivateService {
    List<UserPrivateDTO> getAll() throws ErrorProcessException;
    UserPrivateDTO findById(Integer id) throws ErrorProcessException;
    UserPrivate findByMail(String mail) throws ErrorProcessException;
    UserPrivateDTO save(Integer idUser, UserPrivate userPrivate) throws ErrorProcessException;
    UserPrivateDTO save(UserPrivate userPrivate, Integer idToUpdate) throws ErrorProcessException;
    UserPrivateDTO save(UserEmailUpdate userPrivate, Integer idToUpdate) throws ErrorProcessException;
    boolean delete(Integer id) throws ErrorProcessException;
    UserDTO validate(UserPrivate userToValidate) throws ErrorProcessException;
    boolean exists(String mail) throws ErrorProcessException;
}
