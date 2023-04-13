package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.FolderDTO;
import com.textorganicer.negocio.dto.mapper.FolderMapper;
import com.textorganicer.respositorios.FolderRepository;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderServiceImpl implements FolderService {
    private final FolderRepository repository;
    private final UserRepository userRepository;
    private final FolderMapper mapper;

    @Override
    public List<FolderDTO> getAll() throws ErrorProcessException {
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
    public FolderDTO findById(Integer id) throws ErrorProcessException {
        Folder folder = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ninguna carpeta con ese id"));
        try {
            return mapper.entityToDto(folder);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public List<FolderDTO> getAllByUser(Integer idUser) throws ErrorProcessException {
        List<Folder> all = repository
                .getAllByUser(idUser)
                .orElseThrow(() -> new NotFoundException("Este usuario no tiene carpetas"));
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
    public FolderDTO save(Folder folder, Integer idUser) throws ErrorProcessException {
        User user = userRepository
                .findById(idUser)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        try {
            folder.setUser(user);
            log.info("postFolder - " + folder);
            return mapper.entityToDto(repository.save(folder));
        } catch (RuntimeException ex) {
            log.error("postFolder - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public FolderDTO update(Folder folder, Integer id) throws ErrorProcessException {
        Folder folderToUpdate = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Carpeta no encontrada"));
        try {
            folder.setUser(folderToUpdate.getUser());
            folder.setRoles(folderToUpdate.getRoles());
            folder.setPages(folderToUpdate.getPages());
            log.info("updateFolder - " + folder);
            return mapper.entityToDto(repository.save(folder));
        } catch (RuntimeException ex) {
            log.error("updateFolder - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ErrorProcessException {
        Folder folderToDelete = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Carpeta no encontrada"));
        try {
            repository.delete(folderToDelete);
            log.info("deleteFolder - " + id);
            return true;
        } catch (RuntimeException ex) {
            log.error("deleteFolder - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }
}
