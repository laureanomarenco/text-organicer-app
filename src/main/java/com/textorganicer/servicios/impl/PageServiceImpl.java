package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import com.textorganicer.negocio.dto.mapper.PageMapper;
import com.textorganicer.respositorios.FolderRepository;
import com.textorganicer.respositorios.PageRepository;
import com.textorganicer.servicios.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PageServiceImpl implements PageService {
    private final PageRepository repository;
    private final PageMapper mapper;
    private final FolderRepository folderRepository;


    @Override
    public List<PageDTO> getAll() throws ErrorProcessException {
        try {
            return repository
                    .findAll()
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public PageDTO findById(Integer id) throws ErrorProcessException {
        Page page = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ninguna página con ese id"));
        try {
            return mapper.entityToDto(page);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public List<PageDTO> getAllByFolder(Integer idFolder) throws ErrorProcessException {
        List<Page> all = repository
                .getAllByFolder(idFolder)
                .orElseThrow(() -> new NotFoundException("No hay páginas en esta carpeta"));
        try {
            return all
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }

    }

    @Override
    public PageDTO save(Page page, Integer idFolder) throws ErrorProcessException {
        Folder folder = folderRepository
                .findById(idFolder)
                .orElseThrow(() -> new NotFoundException("Carpeta no encontrada"));
        try {
            page.setFolder(folder);
            log.info("postPage - " + page);
            return mapper.entityToDto(repository.save(page));
        } catch (RuntimeException ex) {
            log.error("postPage - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    public PageDTO update(Integer id, Page page) throws ErrorProcessException {
        Page pageToUpdate = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Página no encontrada"));
        try {
            page.setFolder(pageToUpdate.getFolder());
            log.info("updatePage - " + page);
            return mapper.entityToDto(repository.save(page));
        } catch (RuntimeException ex) {
            log.error("updatePage - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ErrorProcessException {
        Page pageToDelete = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Página no encontrado"));
        try {
            repository.delete(pageToDelete);
            log.info("deletePage - " + id);
            return true;
        } catch (RuntimeException ex) {
            log.error("deletePage - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }
}
