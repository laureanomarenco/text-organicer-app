package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Role;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.RoleService;
import com.textorganicer.servicios.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        List<Role> all;

        try {
            all = this.service.getAll();
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", all);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Role> role;

        try {
            role = this.service.findById(id);
            if (!role.isPresent()) {
                throw new RuntimeException("No hay ningún rol con ese id");
            }

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", role);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> newRole(@RequestBody Role role,
                                     @RequestParam Integer id_user,
                                     @RequestParam Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        Role newRole = new Role();

        try {
            Optional<User> user = this.userService.findById(id_user);
            Optional<Folder> folder = this.folderService.findById(id_folder);

            if(!user.isPresent()) throw new RuntimeException("El usuario pasado no existe");

            if(!folder.isPresent()) throw new RuntimeException("La carpeta pasada no existe");

            role.setUser(user.orElseThrow());
            role.setFolder(folder.orElseThrow());

            newRole = this.service.save(role);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newRole);
        return ResponseEntity.ok(res);
    }

    //#TODO fix
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<Role> roleToUpdate = this.service.findById(id);
            if (!roleToUpdate.isPresent()) {
                throw new RuntimeException("El rol no existe");
            }

            this.service.save(role);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", role);

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

}
