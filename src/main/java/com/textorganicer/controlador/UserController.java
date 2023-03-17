package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPostDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.servicios.UserService;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService service;

//    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> res = new HashMap<>();

        List<UserDTO> allDtos;

        try {
            List<User> all = this.service.getAll();
            allDtos = all.stream()
                    .map(UserMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDtos);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

         UserDTO userDTO;

        try {
            Optional<User> user = this.service.findById(id);
            if(!user.isPresent()) {
                throw new RuntimeException("No hay ningún usuario con ese id");
            }
            userDTO = UserMapper.entityToDto(user.orElseThrow());
        log.info("get a usuarios");

        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        Map<String, Object> res = new HashMap<>();

         UserDTO userDTO;

        try {
            Optional<User> user = this.service.findByUsername(username);

            if(!user.isPresent()) {
                throw new RuntimeException("No hay ningún usuario con ese username");
            }

            userDTO = UserMapper.entityToDto(user.get());

        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> newUser(@RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

         UserPostDTO newUserDTO;

        try {
            if(this.service.userExists(user.getUsername())) throw new RuntimeException("El username ya existe");

            User newUser = this.service.save(user);

            newUserDTO = UserMapper.entityToPostDto(newUser);

            log.info("usuario nuevo: " + newUser.toString());
        } catch (RuntimeException ex){
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newUserDTO);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
        Map<String, Object> res = new HashMap<>();

        UserDTO userDTO;

        try {
            Optional<User> userToUpdate = this.service.findById(id);
            if(!userToUpdate.isPresent()) throw new RuntimeException("El usuario no existe");

            user.setUserPrivate(userToUpdate.get().getUserPrivate());
            user.setRoles(userToUpdate.get().getRoles());
            user.setFolders(userToUpdate.get().getFolders());
            User updated = this.service.save(user);

            userDTO = UserMapper.entityToDto(updated);

        } catch (RuntimeException ex){
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userDTO);

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
