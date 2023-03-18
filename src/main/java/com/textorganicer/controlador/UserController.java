package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPostDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserMapper userMapper;
    private final UserService service;

//    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(UserMapper userMapper, UserService service) {
        this.userMapper = userMapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> res = new HashMap<>();

        List<UserDTO> allDtos;

        try {
            List<User> all = this.service.getAll();
            allDtos = all.stream()
                    .map(userMapper::entityToDto)
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
            userDTO = userMapper.entityToDto(user.orElseThrow());
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

            userDTO = userMapper.entityToDto(user.get());

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

            String token = TokenGenerator.generateToken();
            user.setToken(token);

            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            user.setTokenExpiration(expirationDate);

            User newUser = this.service.save(user);

            newUserDTO = userMapper.entityToPostDto(newUser);

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
            user.setToken(userToUpdate.get().getToken());
            user.setTokenExpiration(userToUpdate.get().getTokenExpiration());
            User updated = this.service.save(user);

            userDTO = userMapper.entityToDto(updated);

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

    @PostMapping("/validateToken/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        Map<String, Object> res = new HashMap<>();

        UserDTO newUserDTO;

        try {
            Optional<User> userToValidate = this.service.findByToken(token);
            if(!userToValidate.isPresent()) throw new RuntimeException("Token inexistente");

            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(userToValidate.get().getTokenExpiration())) throw new RuntimeException("Token expirado");

            newUserDTO = this.userMapper.entityToDto(userToValidate.get());


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
}
