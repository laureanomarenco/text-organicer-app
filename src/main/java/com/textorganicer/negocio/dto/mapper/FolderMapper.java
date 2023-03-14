package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.FolderDTO;



public class FolderMapper {
    public static FolderDTO entityToDto(Folder entity) {
        return new FolderDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getIs_public(),
                entity.getUser().getId()
        );
    }
    public static Folder dtoToEntity(FolderDTO dto) {
        Folder folder = new Folder();
        folder.setId(dto.getId());
        folder.setNombre(dto.getNombre());
        folder.setIs_public(dto.getIs_public());
        User user = new User();
        user.setId(dto.getId());
        folder.setUser(user);
        return folder;
    }
}
