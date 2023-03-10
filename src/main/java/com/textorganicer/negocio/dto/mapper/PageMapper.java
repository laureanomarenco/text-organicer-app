package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PageMapper {
    PageDTO entityToDto(Page entity);
    Page DtoToEntity(PageDTO entity);
}
