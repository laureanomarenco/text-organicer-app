package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.FolderDTO;
import com.textorganicer.negocio.dto.mapper.FolderMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/folder")
public class FolderController {

    private final FolderService service;
    private final UserService userService;

    public FolderController(FolderService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllFolders() {
        Map<String, Object> res = new HashMap<>();

        List<FolderDTO> allDTO;

        try {
            List<Folder> all = this.service.getAll();

            allDTO = all.stream()
                    .map(FolderMapper::entityToDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            Optional<Folder> folder = this.service.findById(id);
            if(!folder.isPresent()) {
                throw new RuntimeException("No hay ninguna carpeta con ese id");
            }

            folderDTO = FolderMapper.entityToDto(folder.get());
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", folderDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("byUser/{id_user}")
    public ResponseEntity<?> getAllFolders(@PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        List<FolderDTO> allDTO;

        try {
            Optional<List<Folder>> all = this.service.getAllByUser(id_user);
            if(!all.isPresent()) throw new RuntimeException("Este usuario no tiene carpetas");

            allDTO = all.get().stream()
                    .map(FolderMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id_user}")
    public ResponseEntity<?> newFolder(@RequestBody Folder folder, @PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            //#TODO tiene sentido evitar que se creen repetidas?
            Optional<User> user = this.userService.findById(id_user);

            if(!user.isPresent()){
                throw new RuntimeException("hubo un problema, no se encontró el usuario");
            }
            folder.setUser(user.orElseThrow());
            Folder newFolder = this.service.save(folder);

            folderDTO = FolderMapper.entityToDto(newFolder);
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", folderDTO);
        res.put("status", HttpStatus.CREATED);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Integer id, @RequestBody Folder folder) {
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            Optional<Folder> folderToUpdate = this.service.findById(id);
            if(!folderToUpdate.isPresent()) throw new RuntimeException("La carpeta no existe");

            folder.setUser(folderToUpdate.get().getUser());
            folder.setRoles(folderToUpdate.get().getRoles());
            folder.setPages(folderToUpdate.get().getPages());

            Folder updated = this.service.save(folder);
            folderDTO = FolderMapper.entityToDto(updated);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", folderDTO);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Folder> folder;

        try {
            folder = this.service.findById(id);
            if(!folder.isPresent()) throw new RuntimeException("La carpeta no existe");


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

