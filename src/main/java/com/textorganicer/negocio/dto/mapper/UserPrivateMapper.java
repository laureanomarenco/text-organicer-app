package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserPrivateDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPrivateMapper {
    @Mapping(source = "user.id", target = "user_id")
    UserPrivateDTO entityToDto(UserPrivate entity);

    @InheritInverseConfiguration
    UserPrivate dtoToEntity(UserPrivateDTO dto);

}