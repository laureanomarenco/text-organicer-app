package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Folder;
import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.negocio.dto.PageDTO;
import com.textorganicer.negocio.dto.mapper.PageMapper;
import com.textorganicer.servicios.FolderService;
import com.textorganicer.servicios.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/page")
public class PageController {
    private final PageService service;
    private final FolderService folderService;


    public PageController(PageService service, FolderService folderService) {
        this.service = service;
        this.folderService = folderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPages() {
        Map<String, Object> res = new HashMap<>();

        List<PageDTO> allDTO;

        try {
            List<Page> all = this.service.getAll();

            allDTO = all.stream()
                    .map(PageMapper::entityToDto)
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
    public ResponseEntity<?> getPageById(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

         PageDTO pageDTO;

        try {
            Optional<Page> page = this.service.findById(id);
            if (!page.isPresent()) {
                throw new RuntimeException("No hay ninguna página con ese id");
            }

            pageDTO = PageMapper.entityToDto(page.get());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", pageDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/{id_folder}")
    public ResponseEntity<?> newPage(@RequestBody Page page, @PathVariable Integer id_folder) {
        Map<String, Object> res = new HashMap<>();

        PageDTO newPageDTO;

        try {
            Optional<Folder> folder = this.folderService.findById(id_folder);
            if(!folder.isPresent()){
                throw new RuntimeException("hubo un problema, no se encontró la carpeta");
            }
            page.setFolder(folder.orElseThrow());
            Page newPage = this.service.save(page);

            newPageDTO = PageMapper.entityToDto(newPage);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newPageDTO);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePage(@PathVariable Integer id, @RequestBody Page page) {
        Map<String, Object> res = new HashMap<>();

        PageDTO pageDTO;

        try {
            Optional<Page> pageToUpdate = this.service.findById(id);
            if (!pageToUpdate.isPresent()) throw new RuntimeException("La página no existe");

            page.setFolder(pageToUpdate.get().getFolder());
            Page updated = this.service.save(page);
            pageDTO = PageMapper.entityToDto(updated);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", pageDTO);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePage(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Page> page;

        try {
            page = this.service.findById(id);
            if (!page.isPresent()) {
                throw new RuntimeException("La página no existe");
            }

            this.service.delete(page.orElseThrow());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);

        return ResponseEntity.ok(res);
    }

}
