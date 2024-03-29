package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dto.FolderDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(target = "id_user", source = "user.id")
    FolderDTO entityToDto(Folder entity);

    @InheritInverseConfiguration
    @Mapping(target = "user", ignore = true)
    Folder dtoToEntity(FolderDTO dto);

}
