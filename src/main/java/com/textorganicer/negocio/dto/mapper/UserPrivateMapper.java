package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserPrivateDTO;



public class UserPrivateMapper {
    public static UserPrivateDTO entityToDto(UserPrivate entity) {
        return new UserPrivateDTO(
                entity.getId(),
                entity.getUser().getId(),
                entity.getMail(),
                entity.getPassword()
        );
    }

    public static UserPrivate DtoToEntity(UserPrivateDTO entity) {
        entity.getId();

        User user = new User();
        user.setId(entity.getUser_id());

        UserPrivate userPrivate = new UserPrivate();
        userPrivate.setUser(user);
        userPrivate.setMail(entity.getMail());
        userPrivate.setPassword(entity.getPassword());

        return userPrivate;
    }

}