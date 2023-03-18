package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.FolderDTO;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPostDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final FolderMapper folderMapper;

    public UserMapper(FolderMapper folderMapper) {
        this.folderMapper = folderMapper;
    }


    public UserDTO entityToDto(User entity) {
        List<Folder> all = entity.getFolders();
        List<FolderDTO> allDTO = all.stream()
                .map(folderMapper::entityToDto)
                .collect(Collectors.toList());
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getImagen(),
                entity.getToken(),
                allDTO
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
        entity.setUserPrivate(userPrivate);
        return entity;
    }
}
