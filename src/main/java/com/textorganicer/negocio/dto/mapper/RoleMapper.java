package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.RoleDTO;

public class RoleMapper {
    public static RoleDTO entityToDto(Role entity) {
        return new RoleDTO(
                entity.getId(),
                entity.getUser().getId(),
                entity.getFolder().getId(),
                entity.getRole_type()
        );
    }

    public static Role dtoToEntity(RoleDTO dto) {
        Role entity = new Role();
        entity.setId(dto.getId());

        User user = new User();
        user.setId(dto.getId());
        entity.setUser(user);

        Folder folder = new Folder();
        folder.setId(dto.getId());
        entity.setFolder(folder);

        entity.setRole_type(dto.getRole_type());

        return entity;
    }
}
