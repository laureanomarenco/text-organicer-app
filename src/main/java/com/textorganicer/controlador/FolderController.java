package com.textorganicer.controlador;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.FolderDTO;
import com.textorganicer.negocio.dto.mapper.FolderMapper;
import com.textorganicer.servicios.FolderService;
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
 * Controlador de Carpetas "/folder"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/folder")
@Slf4j
public class FolderController {

    private final FolderService service;
    private final UserService userService;
    private final FolderMapper folderMapper;


    public FolderController(FolderService service, UserService userService, FolderMapper folderMapper) {
        this.service = service;
        this.userService = userService;
        this.folderMapper = folderMapper;
    }

    /**
     * getAll de Folders "/folder/"
     * @return List<FolderDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllFolders() {
        Map<String, Object> res = new HashMap<>();

        List<FolderDTO> allDTO;

        try {
            List<Folder> all = this.service.getAll();

            allDTO = all.stream()
                    .map(folderMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    /**
     * getByID de Folder "folder/{id}"
     * @param id
     * @return FolderDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            Optional<Folder> folder = this.service.findById(id);
            if(!folder.isPresent()) {
                throw new NotFoundException("No hay ninguna carpeta con ese id");
            }

            folderDTO = folderMapper.entityToDto(folder.get());

        } catch (NotFoundException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", folderDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Get folders by id_user "/folder/{id_user}"
     * @param id_user
     * @return List<FolderDTO>
     */
    @GetMapping("byUser/{id_user}")
    public ResponseEntity<?> getAllFolders(@PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        List<FolderDTO> allDTO;

        try {
            Optional<List<Folder>> all = this.service.getAllByUser(id_user);
            if(!all.isPresent()) throw new NotFoundException("Este usuario no tiene carpetas");

            allDTO = all.get().stream()
                    .map(folderMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (NotFoundException ex) {

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    /**
     * Post de folder "/folder/{id_user}"
     * @param folder
     * @param id_user
     * @return FolderDTO
     */
    @PostMapping("/{id_user}")
    public ResponseEntity<?> newFolder(@RequestBody Folder folder, @PathVariable Integer id_user) {
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            Optional<User> user = this.userService.findById(id_user);

            if(!user.isPresent()){
                throw new NotFoundException("hubo un problema, no se encontró el usuario");
            }
            folder.setUser(user.orElseThrow());
            Folder newFolder = this.service.save(folder);

            folderDTO = folderMapper.entityToDto(newFolder);
        } catch (NotFoundException ex) {

            log.error("newFolder - Usuario no encontrado");

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        log.info("newFolder - " + folderDTO.toString());

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.CREATED);
        res.put("data", folderDTO);
        return ResponseEntity.ok(res);
    }

    /**
     * Update Folder "/folder/{id}"
     * @param id
     * @param folder
     * @return FolderDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(@PathVariable Integer id, @RequestBody Folder folder) {
        Map<String, Object> res = new HashMap<>();

        FolderDTO folderDTO;

        try {
            Optional<Folder> folderToUpdate = this.service.findById(id);
            if(!folderToUpdate.isPresent()) throw new NotFoundException("La carpeta no existe");

            folder.setUser(folderToUpdate.get().getUser());
            folder.setRoles(folderToUpdate.get().getRoles());
            folder.setPages(folderToUpdate.get().getPages());

            Folder updated = this.service.save(folder);
            folderDTO = folderMapper.entityToDto(updated);

        } catch (NotFoundException ex){
            log.error("updateFolder - Carpeta inexistente");

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        log.info("updateFolder: " + folderDTO.toString());

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);
        res.put("data", folderDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Delete Folder "/folder/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Folder> folder;

        try {
            folder = this.service.findById(id);
            if(!folder.isPresent()) throw new NotFoundException("La carpeta no existe");


            this.service.delete(folder.orElseThrow());

        } catch (NotFoundException ex){

            log.error("deleteFolder - Carpeta no encontrada");

            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        log.info("deleteFolder - " + id);

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);

        return ResponseEntity.ok(res);
    }

}

