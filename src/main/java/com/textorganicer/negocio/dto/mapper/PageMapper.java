package com.textorganicer.negocio.dto.mapper;


import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PageMapper {
    @Mapping(source = "folder.id", target = "id_folder")
    PageDTO entityToDto(Page entity);

    @InheritInverseConfiguration
    Page dtoToEntity(PageDTO dto);
}
