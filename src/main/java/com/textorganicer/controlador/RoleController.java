package com.textorganicer.controlador;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dto.mapper.RoleMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.RoleService;
import com.textorganicer.servicios.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador de Roles "/role"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleMapper roleMapper;
    private final RoleService service;
    private final UserService userService;
    private final FolderService folderService;


    /**
     * getAll de Roles "/role/"
     * @return List<RoleDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllRoles() throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    /**
     * getByID de Role "role/{id}"
     * @param id
     * @return RoleDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }


    /**
     * Get roles by id_user "/role/byUser/{id_user}"
     * @param id_user
     * @return List<RoleDTO>
     */
    @GetMapping("/byUser/{id_user}")
    public ResponseEntity<?> getAllByUserId(
            @PathVariable Integer id_user
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findShared(id_user));
    }


    /**
     * Get roles by id_folder "/role/byFolder/{id_folder}"
     * @param id_folder
     * @return List<RoleDTO>
     */
    @GetMapping("/byFolder/{id_folder}")
    public ResponseEntity<?> getAllByFolderId(
            @PathVariable Integer id_folder
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllByFolderId(id_folder));
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
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByUserAndFolder(id_user, id_folder));
    }



    /**
     * Post de roles "/role?id_user=xxxx&id_folder=xxxx"
     * @param role
     * @param id_user
     * @param id_folder
     * @return
     */
    @PostMapping
    public ResponseEntity<?> newRole(
            @RequestBody Role role,
            @RequestParam Integer id_user,
            @RequestParam Integer id_folder
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.save(role, id_user, id_folder));
    }


    /**
     * Update de role "/role/{id}"
     * @param id
     * @param role
     * @return RoleDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Integer id,
            @RequestBody Role role
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(id, role));
    }


    /**
     * delete de Role "/role/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));
    }
}
