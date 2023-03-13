package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;

import com.textorganicer.servicios.UserPrivateService;
import com.textorganicer.servicios.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/user_private")
public class UserPrivateController {

    private final UserPrivateService service;
    private final UserService userService;

    public UserPrivateController(UserPrivateService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsersPrivate() {
        Map<String, Object> res = new HashMap<>();

        List<UserPrivate> all;

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
    public ResponseEntity<?> getUserPrivateById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        Optional<UserPrivate> userPrivate;

        try {
            userPrivate = this.service.findById(id);
            if(!userPrivate.isPresent()) {
                throw new RuntimeException("No hay ningún usuario con ese id");
            }

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userPrivate);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<?> newUserPrivate(@RequestBody UserPrivate userPrivate, @PathVariable Integer user_id) {
        Map<String, Object> res = new HashMap<>();

        UserPrivate newUserPrivate;

        try {
            if(this.service.findByMail(userPrivate.getMail()).isPresent()) {
                throw new RuntimeException("El username ya existe");
            }
            Optional<User> user = this.userService.findById(user_id);
            if(!user.isPresent()){
                throw new RuntimeException("hubo un problema, no se creo bien el usuario público");
            }
            userPrivate.setUser(user.orElseThrow());
            newUserPrivate = this.service.save(userPrivate);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newUserPrivate);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserPrivate(@PathVariable Integer id,@RequestBody UserPrivate userPrivate) {
        Map<String, Object> res = new HashMap<>();

        try {
            Optional<UserPrivate> userToUpdate = this.service.findById(id);
            if(!userToUpdate.isPresent()) {
                throw new RuntimeException("El usuario no existe");
            }

            this.service.save(userPrivate);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userPrivate);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserPrivate(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();

        Optional<UserPrivate> userPrivate;

        try {
            userPrivate = this.service.findById(id);
            if(!userPrivate.isPresent()) {
                throw new RuntimeException("El usuario no existe");
            }

            this.service.delete(userPrivate.orElseThrow());

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserPrivate userPrivate) {
        Map<String, Object> res = new HashMap<>();

        User validatedUser;

        try {
            Optional<UserPrivate> userToValidate = this.service.findByMail(userPrivate.getMail());
            if(!userToValidate.isPresent()) throw new RuntimeException("Mail incorrecto o usuario inexistente");

            boolean isValid = this.service.validate(userPrivate, userToValidate);

            if(!isValid) throw new RuntimeException("Mail incorrecto o usuario inexistente");
            else {
                validatedUser = userToValidate.orElseThrow().getUser();
            }


        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", validatedUser);
        return ResponseEntity.ok(res);
    }

}
