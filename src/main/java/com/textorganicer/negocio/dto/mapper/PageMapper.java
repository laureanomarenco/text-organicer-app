package com.textorganicer.negocio.dto.mapper;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;


public class PageMapper {
    public static PageDTO entityToDto(Page entity) {
      return new PageDTO(
              entity.getId(),
              entity.getOrder(),
              entity.getTitulo(),
              entity.getSubtitulo(),
              entity.getFirma(),
              entity.getContenido(),
              entity.getFolder().getId()

      );
    };
    public static Page dtoToEntity(PageDTO dto) {
        Page entity = new Page();
        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setSubtitulo(dto.getSubtitulo());
        entity.setFirma(dto.getFirma());
        entity.setContenido(dto.getContenido());
        Folder folder = new Folder();
        folder.setId(dto.getId());
        entity.setFolder(folder);
        return entity;
    };
}
