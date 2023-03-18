package com.textorganicer.negocio.dto.mapper;


import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PageMapper {
    @Mapping(source = "folder.id", target = "id_folder")
    PageDTO entityToDto(Page entity);

    @Mapping(source = "id_folder", target = "folder.id")
    Page dtoToEntity(PageDTO dto);
}
