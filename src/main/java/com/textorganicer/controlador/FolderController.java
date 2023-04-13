package com.textorganicer.controlador;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**----------------------------------------------
 * Controlador de Carpetas "/folder"
 ----------------------------------------------*/
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService service;


    /**
     * getAll de Folders "/folder/"
     * @return List<FolderDTO>
    */
    @GetMapping
    public ResponseEntity<?> getAllFolders() throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    /**
     * getByID de Folder "folder/{id}"
     * @param id
     * @return FolderDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFolderById(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }


    /**
     * Get folders by id_user "/folder/{id_user}"
     * @param id_user
     * @return List<FolderDTO>
     */
    @GetMapping("byUser/{id_user}")
    public ResponseEntity<?> getAllFolders(
            @PathVariable Integer id_user
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllByUser(id_user));
    }


    /**
     * Post de folder "/folder/{id_user}"
     * @param folder
     * @param id_user
     * @return FolderDTO
     */
    @PostMapping("/{id_user}")
    public ResponseEntity<?> newFolder(
            @RequestBody Folder folder,
            @PathVariable Integer id_user
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.save(folder, id_user));
    }


    /**
     * Update Folder "/folder/{id}"
     * @param id
     * @param folder
     * @return FolderDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFolder(
            @PathVariable Integer id,
            @RequestBody Folder folder
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.save(folder, id));
    }


    /**
     * Delete Folder "/folder/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));
    }
}