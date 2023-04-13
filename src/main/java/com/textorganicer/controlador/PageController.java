package com.textorganicer.controlador;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.servicios.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador de Páginas "/page"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/page")
@RequiredArgsConstructor
public class PageController {
    private final PageService service;


    /**
     * getAll de Page "/page/"
     * @return List<PageDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllPages() throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    /**
     * getByID de Page "page/{id}"
     * @param id
     * @return PageDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }


    /**
     * Get pages by id_folder "/page/{id_folder}"
     * @param id_folder
     * @return List<PageDTO>
     */
    @GetMapping("byFolder/{id_folder}")
    public ResponseEntity<?> getAllPages(
            @PathVariable Integer id_folder
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllByFolder(id_folder));
    }


    /**
     * Post de page "/page/{id_folder}"
     * @param page
     * @param id_folder
     * @return PageDTO
     */
    @PostMapping("/{id_folder}")
    public ResponseEntity<?> newPage(
            @RequestBody Page page,
            @PathVariable Integer id_folder
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.save(page, id_folder));
    }


    /**
     * Update Page "/page/{id}"
     * @param id
     * @param page
     * @return pageDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePage(
            @PathVariable Integer id,
            @RequestBody Page page
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(id, page));
    }


    /**
     * Delete Page "/page/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePage(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));
    }
}