package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPostDTO;

public class UserMapper {
    public static UserDTO entityToDto(User entity) {
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getImagen(),
                entity.getUserPrivate().getId()
        );
    }

    public static UserPostDTO entityToPostDto(User entity) {
        return new UserPostDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getImagen()
        );
    }

    public static User DtoToEntity(UserDTO dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setImagen(dto.getImagen());
        UserPrivate userPrivate = new UserPrivate();
        userPrivate.setId(dto.getIdUserPrivate());
        entity.setUserPrivate(userPrivate);
        return entity;
    }
}
