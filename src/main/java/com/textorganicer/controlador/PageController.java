package com.textorganicer.controlador;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import com.textorganicer.negocio.dto.mapper.PageMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.PageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador de Páginas "/page"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/page")
@Slf4j
public class PageController {

    private final PageMapper pageMapper;
    private final PageService service;
    private final FolderService folderService;


    public PageController(PageMapper pageMapper, PageService service, FolderService folderService) {
        this.pageMapper = pageMapper;
        this.service = service;
        this.folderService = folderService;
    }



    /**
     * getAll de Page "/page/"
     * @return List<PageDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllPages() {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<PageDTO> allDTO;

        try {
            // GET & MAP
            List<Page> all = this.service.getAll();
            allDTO = all.stream()
                    .map(pageMapper::entityToDto)
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
     * getByID de Page "page/{id}"
     * @param id
     * @return PageDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPageById(@PathVariable Integer id) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        PageDTO pageDTO;

        try {
            // FIND & MAP
            Page page = this.service.findById(id);
            pageDTO = pageMapper.entityToDto(page);

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
        res.put("data", pageDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Get pages by id_folder "/page/{id_folder}"
     * @param id_folder
     * @return List<PageDTO>
     */
    @GetMapping("byFolder/{id_folder}")
    public ResponseEntity<?> getAllPages(@PathVariable Integer id_folder) {
        // CONSTANT  OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<PageDTO> allDTO;

        try {
            // FIND & MAP
            List<Page> all = this.service.getAllByFolder(id_folder);
            allDTO = all.stream()
                    .map(pageMapper::entityToDto)
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
     * Post de page "/page/{id_folder}"
     * @param page
     * @param id_folder
     * @return PageDTO
     */
    @PostMapping("/{id_folder}")
    public ResponseEntity<?> newPage(@RequestBody Page page, @PathVariable Integer id_folder) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        PageDTO newPageDTO;

        try {
            // FIND & SET
            Folder folder = this.folderService.findById(id_folder);
            page.setFolder(folder);
            // SAVE & MAP
            Page newPage = this.service.save(page);
            newPageDTO = pageMapper.entityToDto(newPage);

            // ERROR
        } catch (NotFoundException ex) {
            log.error("newPage - Carpeta no encontrada");
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        log.info("newFolder - " + newPageDTO.toString());
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.CREATED);
        res.put("data", newPageDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Update Page "/page/{id}"
     * @param id
     * @param page
     * @return pageDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePage(@PathVariable Integer id, @RequestBody Page page) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        PageDTO pageDTO;

        try {
            // FIND & SET
            Page pageToUpdate = this.service.findById(id);
            page.setFolder(pageToUpdate.getFolder());
            // SAVE & MAP
            Page updated = this.service.save(page);
            pageDTO = pageMapper.entityToDto(updated);

            // ERROR
        } catch (NotFoundException ex) {
            log.error("updatePage - Página inexistente");
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        log.info("updatePage: " + id);
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);
        res.put("data", pageDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Delete Page "/page/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePage(@PathVariable Integer id) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        Page page;

        try {
            // FIND & DELETE
            page = this.service.findById(id);
            this.service.delete(page);

            // ERROR
        } catch (NotFoundException ex) {
            log.error("deletePage - Página inexistente");
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

        // SUCCESS
        log.info("deletePage - " + id);
        res.put("status", HttpStatus.ACCEPTED);
        res.put("success", Boolean.TRUE);
        return ResponseEntity.ok(res);
    }
}