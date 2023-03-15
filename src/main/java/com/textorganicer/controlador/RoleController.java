package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.RoleDTO;
import com.textorganicer.negocio.dto.mapper.RoleMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.RoleService;
import com.textorganicer.servicios.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService service;
    private final UserService userService;
    private final FolderService folderService;

    public RoleController(RoleService service, UserService userService, FolderService folderService) {
        this.service = service;
        this.userService = userService;
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        Map<String, Object> res = new HashMap<>();
         List<RoleDTO> allDTO;

        try {
            List<Role> all = this.service.getAll();
            allDTO = all.stream()
                    .map(RoleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> role = this.service.findById(id);
            if (!role.isPresent()) {
                throw new RuntimeException("No hay ningún rol con ese id");
            }
            roleDTO = RoleMapper.entityToDto(role.get());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/byUser/{id_user}")
    public ResponseEntity<?> getAllByUserId(@PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        List<RoleDTO> allDTO;

        try {
            Optional<List<Role>> roles = this.service.findShared(id_user);
            if (!roles.isPresent()) throw new RuntimeException("Ese usuario no tiene roles");

            allDTO = roles.get().stream()
                    .map(RoleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/byFolder/{id_folder}")
    public ResponseEntity<?> getAllByFolderId(@PathVariable Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        List<RoleDTO> allDTO;

        try {
            Optional<List<Role>> roles = this.service.getAllByFolderId(id_folder);
            if (!roles.isPresent()) throw new RuntimeException("Esta carpeta no tiene roles");

            allDTO = roles.get().stream()
                    .map(RoleMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/byUserAndFolder")
    public ResponseEntity<?> getAllByUserId(@RequestParam Integer id_user,
                                            @RequestParam Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> role = this.service.findByUserAndFolder(id_user, id_folder);
            if (!role.isPresent()) throw new RuntimeException("No hay roles");

            roleDTO = RoleMapper.entityToDto(role.get());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> newRole(@RequestBody Role role,
                                     @RequestParam Integer id_user,
                                     @RequestParam Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO newRoleDTO;

        try {
            Optional<User> user = this.userService.findById(id_user);
            Optional<Folder> folder = this.folderService.findById(id_folder);

            if(!user.isPresent()) throw new RuntimeException("El usuario pasado no existe");

            if(!folder.isPresent()) throw new RuntimeException("La carpeta pasada no existe");

            role.setUser(user.orElseThrow());
            role.setFolder(folder.orElseThrow());

            Role newRole = this.service.save(role);
            newRoleDTO = RoleMapper.entityToDto(newRole);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newRoleDTO);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        Map<String, Object> res = new HashMap<>();

        RoleDTO roleDTO;

        try {
            Optional<Role> roleToUpdate = this.service.findById(id);
            if (!roleToUpdate.isPresent()) throw new RuntimeException("El rol no existe");

            role.setUser(roleToUpdate.get().getUser());
            role.setFolder(roleToUpdate.get().getFolder());
            Role updated = this.service.save(role);
            roleDTO = RoleMapper.entityToDto(updated);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", roleDTO);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Role> role;

        try {
            role = this.service.findById(id);
            if (!role.isPresent()) {
                throw new RuntimeException("El rol no existe");
            }

            this.service.delete(role.orElseThrow());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);

        return ResponseEntity.ok(res);
    }

    //'role?id_user=' + id_user + '&id_folder=' + id_folder

}
