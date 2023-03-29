package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dto.RoleDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "user.id", target = "id_user")
    @Mapping(source = "folder.id", target = "id_folder")
    RoleDTO entityToDto(Role entity);

    @InheritInverseConfiguration
    Role dtoToEntity(RoleDTO dto);

}