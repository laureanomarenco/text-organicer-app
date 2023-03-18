package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.FolderDTO;
import com.textorganicer.negocio.dto.PageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(target = "id_user", source = "user.id")
    FolderDTO entityToDto(Folder entity);

    Folder dtoToEntity(FolderDTO dto);

}
