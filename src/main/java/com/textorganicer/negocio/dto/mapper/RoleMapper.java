package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "user.id", target = "id_user")
    @Mapping(source = "folder.id", target = "id_folder")
    RoleDTO entityToDto(Role entity);

    @Mapping(source = "id_user", target = "user.id")
    @Mapping(source = "id_folder", target = "folder.id")
    Role dtoToEntity(RoleDTO dto);

}