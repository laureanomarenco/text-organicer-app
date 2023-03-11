package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.Page;
import com.textorganicer.servicios.PageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/page")
public class PageController {

    private final PageServiceImpl service;

    public PageController(PageServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllPages() {
        Map<String, Object> res = new HashMap<>();
        List<Page> all;

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
    public ResponseEntity<?> getPageById(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<Page> page;

        try {
            page = this.service.findById(id);
            if (!page.isPresent()) {
                throw new RuntimeException("No hay ninguna página con ese id");
            }

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", page);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> newPage(@RequestBody Page page) {
        Map<String, Object> res = new HashMap<>();

        Page newPage;

        try {
            newPage = this.service.save(page);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newPage);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePage(@PathVariable Integer id, @RequestBody Page page) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<Page> pageToUpdate = this.service.findById(id);
            if (!pageToUpdate.isPresent()) {
                throw new RuntimeException("La página no existe");
            }

            this.service.save(page);

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest().body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", page);

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
