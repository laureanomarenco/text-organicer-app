package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dto.FolderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    FolderDTO entityToDto(Folder entity);
    Folder DtoToEntity(FolderDTO entity);
}
