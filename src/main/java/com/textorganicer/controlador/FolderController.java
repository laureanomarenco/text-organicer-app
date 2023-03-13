package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService service;
    private final UserService userService;

    public FolderController(FolderService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }
    //#TODO agregar dto porque rompe
    @GetMapping
    public ResponseEntity<?> getAllFolders() {
        Map<String, Object> res = new HashMap<>();
        List<Folder> all;

        try {
            all = this.service.getAll();
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", all);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        Optional<Folder> folder;

        try {
            folder = this.service.findById(id);
            if(!folder.isPresent()) {
                throw new RuntimeException("No hay ninguna carpeta con ese id");
            }

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", folder);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id_user}")
    public ResponseEntity<?> newFolder(@RequestBody Folder folder, @PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        Folder newFolder;

        try {
            //#TODO tiene sentido evitar que se creen repetidas?
            Optional<User> user = this.userService.findById(id_user);

            if(!user.isPresent()){
                throw new RuntimeException("hubo un problema, no se encontró el usuario");
            }
            folder.setUser(user.orElseThrow());
            newFolder = this.service.save(folder);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newFolder);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Integer id,@RequestBody Folder folder) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<Folder> folderToUpdate = this.service.findById(id);
            if(!folderToUpdate.isPresent()) {
                throw new RuntimeException("La carpeta no existe");
            }

            this.service.save(folder);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", folder);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Folder> folder;

        try {
            folder = this.service.findById(id);
            if(!folder.isPresent()) {
                throw new RuntimeException("La carpeta no existe");
            }

            this.service.delete(folder.orElseThrow());

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);

        return ResponseEntity.ok(res);
    }

}

