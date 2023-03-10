package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPrivateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPrivateMapper {
    UserPrivateDTO entityToDto(UserPrivate entity);

    UserDTO DtoToEntity(UserPrivateDTO entity);

}