package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.servicios.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> res = new HashMap<>();

        List<User> all;

        try {
            all = this.service.getAll();
        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", all);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        Optional<User> user;

        try {
            user = this.service.findById(id);
            if(!user.isPresent()) {
                throw new RuntimeException("No hay ningún usuario con ese id");
            }

        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", user);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> user;

        try {
            user = this.service.findByUsername(username);
            if(!user.isPresent()) {
                throw new RuntimeException("No hay ningún usuario con ese username");
            }
        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", user);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> newUser(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        User newUser;

        try {
            if(this.service.userExists(user.getUsername())) {
                throw new RuntimeException("El username ya existe");
            }

            newUser = this.service.save(user);

        } catch (RuntimeException ex){
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newUser);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<User> userToUpdate = this.service.findById(id);
            if(!userToUpdate.isPresent()) {
                throw new RuntimeException("El usuario no existe");
            }

            this.service.save(user);

        } catch (RuntimeException ex){
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", user);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> user;

        try {
            user = this.service.findById(id);
            if(!user.isPresent()) {
                throw new RuntimeException("El usuario no existe");
            }

            this.service.delete(user.orElseThrow());

        } catch (RuntimeException ex){
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);

        return ResponseEntity.ok(res);
    }
}
