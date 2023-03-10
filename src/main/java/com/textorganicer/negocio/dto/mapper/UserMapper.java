package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO entityToDto(User entity);

    User DtoToEntity(UserDTO entity);
}
