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
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<RoleDTO> allDTO;

        try {
            // GET & MAP
            List<Role> all = this.service.getAll();
            allDTO = all.stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

            // ERROR
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
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
    public ResponseEntity<?> getRoleById(
            @PathVariable Integer id
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        RoleDTO roleDTO;

        try {
            // FIND & MAP
            Role role = this.service.findById(id);
            roleDTO = roleMapper.entityToDto(role);

            // ERROR
        } catch (NotFoundException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
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
    public ResponseEntity<?> getAllByUserId(
            @PathVariable Integer id_user
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<RoleDTO> allDTO;

        try {
            // FIND & MAP
            List<Role> roles = this.service.findShared(id_user);
            allDTO = roles.stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

            // ERROR
        } catch (NotFoundException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
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
    public ResponseEntity<?> getAllByFolderId(
            @PathVariable Integer id_folder
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<RoleDTO> allDTO;

        try {
            // FIND & MAP
            List<Role> roles = this.service.getAllByFolderId(id_folder);
            allDTO = roles.stream()
                    .map(roleMapper::entityToDto)
                    .collect(Collectors.toList());

            // ERROR
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
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
    public ResponseEntity<?> getAllByUserId(
            @RequestParam Integer id_user,
            @RequestParam Integer id_folder
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        RoleDTO roleDTO;

        try {
            // FIND & MAP
            Role role = this.service.findByUserAndFolder(id_user, id_folder);
            roleDTO = roleMapper.entityToDto(role);

            // ERROR
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", roleDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Post de roles "/role?id_user=xxxx&id_folder=xxxx"
     * @param roleDTO
     * @param id_user
     * @param id_folder
     * @return
     */
    @PostMapping
    public ResponseEntity<?> newRole(
            @RequestBody RoleDTO roleDTO,
            @RequestParam Integer id_user,
            @RequestParam Integer id_folder
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        RoleDTO newRoleDTO;

        try {
            // FIND & SET
            User user = this.userService.findById(id_user);
            Folder folder = this.folderService.findById(id_folder);

            Role role = this.roleMapper.dtoToEntity(roleDTO);
            role.setUser(user);
            role.setFolder(folder);

            //SAVE & MAP
            Role newRole = this.service.save(role);
            newRoleDTO = roleMapper.entityToDto(newRole);

            // ERROR
        } catch (NotFoundException ex) {
            log.error("newRole - " + ex.getMessage());
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        log.info("newRole - " + newRoleDTO.toString());
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.CREATED);
        res.put("data", newRoleDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Update de role "/role/{id}"
     * @param id
     * @param roleDTO
     * @return RoleDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Integer id,
            @RequestBody RoleDTO roleDTO
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();

        try {
            // FIND & SET
            Role roleToUpdate = this.service.findById(id);
            Role role = this.roleMapper.dtoToEntity(roleDTO);
            role.setUser(roleToUpdate.getUser());
            role.setFolder(roleToUpdate.getFolder());
            // SAVE & MAP
            Role updated = this.service.save(role);
            roleDTO = roleMapper.entityToDto(updated);

            //ERROR
        } catch (NotFoundException ex) {
            log.error("updateRole - " + ex.getMessage());
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
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
    public ResponseEntity<?> deleteRole(
            @PathVariable Integer id
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        Role role;

        try {
            // FIND & DELETE
            role = this.service.findById(id);
            this.service.delete(role);

            // ERROR
        } catch (NotFoundException ex) {
            log.error("deleteRole - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        log.info("deleteRole - " + id);
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);
        return ResponseEntity.ok(res);
    }

}
