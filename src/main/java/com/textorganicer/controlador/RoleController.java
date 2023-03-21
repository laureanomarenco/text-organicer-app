package com.textorganicer.controlador;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.RoleDTO;
import com.textorganicer.negocio.dto.mapper.RoleMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.RoleService;
import com.textorganicer.servicios.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador de Roles "/role"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    private final RoleMapper roleMapper;
    private final RoleService service;
    private final UserService userService;
    private final FolderService folderService;

    public RoleController(RoleMapper roleMapper, RoleService service, UserService userService, FolderService folderService) {
        this.roleMapper = roleMapper;
        this.service = service;
        this.userService = userService;
        this.folderService = folderService;
    }

    /**
     * getAll de Roles "/role/"
     * @return List<RoleDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        Map<String, Object> res = new HashMap<>();
         List<RoleDTO> allDTO;

        try {
            List<Role> all = this.service.getAll();
            allDTO = all.stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    /**
     * getByID de Role "role/{id}"
     * @param id
     * @return RoleDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> role = this.service.findById(id);
            if (!role.isPresent()) {
                throw new NotFoundException("No hay ningún rol con ese id");
            }
            roleDTO = roleMapper.entityToDto(role.get());

        } catch (NotFoundException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Get roles by id_user "/role/byUser/{id_user}"
     * @param id_user
     * @return List<RoleDTO>
     */
    @GetMapping("/byUser/{id_user}")
    public ResponseEntity<?> getAllByUserId(@PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        List<RoleDTO> allDTO;

        try {
            Optional<List<Role>> roles = this.service.findShared(id_user);
            if (!roles.isPresent()) throw new NotFoundException("Ese usuario no tiene roles");

            allDTO = roles.get().stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (NotFoundException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Get roles by id_folder "/role/byFolder/{id_folder}"
     * @param id_folder
     * @return List<RoleDTO>
     */
    @GetMapping("/byFolder/{id_folder}")
    public ResponseEntity<?> getAllByFolderId(@PathVariable Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        List<RoleDTO> allDTO;

        try {
            Optional<List<Role>> roles = this.service.getAllByFolderId(id_folder);
            if (!roles.isPresent()) throw new NotFoundException("Esta carpeta no tiene roles");

            allDTO = roles.get().stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Get roles by id_user y id_folder "/role/byUserAndFolder"
     * @param id_user
     * @param id_folder
     * @return RoleDTO
     */
    @GetMapping("/byUserAndFolder")
    public ResponseEntity<?> getAllByUserId(@RequestParam Integer id_user,
                                            @RequestParam Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> role = this.service.findByUserAndFolder(id_user, id_folder);
            if (!role.isPresent()) throw new NotFoundException("No hay roles");

            roleDTO = roleMapper.entityToDto(role.get());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Post de roles "/role?id_user=xxxx&id_folder=xxxx"
     * @param role
     * @param id_user
     * @param id_folder
     * @return
     */
    @PostMapping
    public ResponseEntity<?> newRole(@RequestBody Role role,
                                     @RequestParam Integer id_user,
                                     @RequestParam Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO newRoleDTO;

        try {
            Optional<User> user = this.userService.findById(id_user);
            Optional<Folder> folder = this.folderService.findById(id_folder);

            if(!user.isPresent()) throw new NotFoundException("El usuario pasado no existe");

            if(!folder.isPresent()) throw new NotFoundException("La carpeta pasada no existe");

            role.setUser(user.orElseThrow());
            role.setFolder(folder.orElseThrow());

            Role newRole = this.service.save(role);
            newRoleDTO = roleMapper.entityToDto(newRole);

        } catch (NotFoundException ex) {
            log.error("newRole - " + ex.getMessage());

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        log.info("newRole - " + newRoleDTO.toString());

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.CREATED);
        res.put("data", newRoleDTO);
        return ResponseEntity.ok(res);
    }

    /**
     * Update de role "/role/{id}"
     * @param id
     * @param role
     * @return RoleDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> roleToUpdate = this.service.findById(id);
            if (!roleToUpdate.isPresent()) throw new NotFoundException("El rol no existe");

            role.setUser(roleToUpdate.get().getUser());
            role.setFolder(roleToUpdate.get().getFolder());
            Role updated = this.service.save(role);
            roleDTO = roleMapper.entityToDto(updated);

        } catch (NotFoundException ex) {
            log.error("updateRole - " + ex.getMessage());

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }
        log.info("updateRole: " + roleDTO.toString());

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * delete de Role "/role/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Role> role;

        try {
            role = this.service.findById(id);
            if (!role.isPresent()) {
                throw new NotFoundException("El rol no existe");
            }

            this.service.delete(role.orElseThrow());

        } catch (NotFoundException ex) {
            log.error("deleteRole - " + ex.getMessage());

            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }
        log.info("deleteRole - " + id);

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);

        return ResponseEntity.ok(res);
    }

}
