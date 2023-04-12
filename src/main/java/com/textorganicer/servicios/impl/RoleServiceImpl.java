package com.textorganicer.servicios.impl;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.RoleDTO;
import com.textorganicer.negocio.dto.mapper.RoleMapper;
import com.textorganicer.respositorios.FolderRepository;
import com.textorganicer.respositorios.RoleRepository;
import com.textorganicer.respositorios.UserRepository;
import com.textorganicer.servicios.RoleService;
import com.textorganicer.servicios.UserService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final RoleMapper mapper;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Override
    public List<RoleDTO> getAll() throws ErrorProcessException {
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
    public RoleDTO findById(Integer id) throws ErrorProcessException {
        Role role = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningún rol con ese id"));
        try {
            return mapper.entityToDto(role);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public List<RoleDTO> getAllByFolderId(Integer id_folder) throws ErrorProcessException {
        List<Role> roles = repository
                .getAllByFolderId(id_folder)
                .orElseThrow(() -> new NotFoundException("Esta carpeta no tiene roles"));
        try {
            return roles
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public List<RoleDTO> findShared(Integer idUser) throws ErrorProcessException {
        List<Role> roles = repository
                .findShared(idUser)
                .orElseThrow(() -> new NotFoundException("Ese usuario no tiene roles"));
        try {
            return roles
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public RoleDTO findByUserAndFolder(Integer idUser, Integer idFolder) throws ErrorProcessException {
        Role role = repository
                .findByIdAndFolder(idUser, idFolder)
                .orElseThrow(() -> new NotFoundException("No hay roles"));
        try {
            return mapper.entityToDto(role);
        } catch (RuntimeException ex) {
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public RoleDTO save(Role role, Integer idUser, Integer idFolder) throws ErrorProcessException {
        User user = userRepository
                .findById(idUser)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        Folder folder = folderRepository
                .findById(idFolder)
                .orElseThrow(() -> new NotFoundException("Carpeta no encontrada"));
        try {
            role.setUser(user);
            role.setFolder(folder);
            log.info("postRole - " + role);
            return mapper.entityToDto(repository.save(role));
        } catch (RuntimeException ex) {
            log.error("postRole - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public RoleDTO update(Integer id, Role role) throws ErrorProcessException {
        Role roleToUpdate = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Role no encontrado"));
        try {
            role.setUser(roleToUpdate.getUser());
            role.setFolder(roleToUpdate.getFolder());

            log.info("updateRole - " + role);
            return mapper.entityToDto(repository.save(role));
        } catch (RuntimeException ex) {
            log.error("updateRole - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) throws ErrorProcessException {
        Role roleToDelete = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Role no encontrado"));
        try {
            this.repository.delete(roleToDelete);
            log.info("deletedRole - " + id);
            return true;
        } catch (RuntimeException ex) {
            log.error("deleteRole - " + ex.getMessage());
            throw new ErrorProcessException(ex.getMessage());
        }
    }
}
