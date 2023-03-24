package com.textorganicer.controlador;

import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.excepciones.SessionException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPostDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador de User "/user"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserMapper userMapper;
    private final UserService service;

    public UserController(UserMapper userMapper, UserService service) {
        this.userMapper = userMapper;
        this.service = service;
    }



    /**
     * getAll de Users "/user/"
     * @return List<UserDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<UserDTO> allDtos;

        try {
            // FIND & MAP
            List<User> all = this.service.getAll();
            allDtos = all.stream()
                    .map(userMapper::entityToDto)
                    .collect(Collectors.toList());

            // ERROR
        } catch (RuntimeException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDtos);
        return ResponseEntity.ok(res);
    }



    /**
     * getByID de User "user/{id}"
     * @param id
     * @return UserDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Integer id
    ){
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserDTO userDTO;

        try {
            // FIND & MAP
            User user = this.service.findById(id);
            userDTO = userMapper.entityToDto(user);

            // ERROR
        } catch (NotFoundException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", userDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * get User by username "user/username?username=xxxx"
     * @param username
     * @return UserDTO
     */
    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(
            @RequestParam String username
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserDTO userDTO;

        try {
            // FIND & MAP
            User user = this.service.findByUsername(username);
            userDTO = userMapper.entityToDto(user);

            // ERROR
        } catch (NotFoundException ex) {
            res.put("sucess", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", userDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Post de User "/user"
     * @param user
     * @return UserPostDTO
     */
    @PostMapping
    public ResponseEntity<?> newUser(
            @RequestBody User user
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserPostDTO newUserDTO;

        try {
            // VALIDATE
            if(this.service.userExists(user.getUsername())) throw new RuntimeException("El username ya existe");

            // SET
            String token = TokenGenerator.generateToken();
            user.setToken(token);
            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            user.setTokenExpiration(expirationDate);

            // SAVE & MAP
            User newUser = this.service.save(user);
            newUserDTO = userMapper.entityToPostDto(newUser);

            // ERROR
        } catch (RuntimeException ex){
            log.error("postUser - " + ex.getMessage());
            res.put("sucess", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("postUser - " + newUserDTO.toString());
        res.put("status", HttpStatus.CREATED);
        res.put("success", Boolean.TRUE);
        res.put("data", newUserDTO);
        return ResponseEntity.ok(res);
    }



    /**
     *
     * @param id
     * @param user
     * @return UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserDTO userDTO;

        try {
            // FIND & SET
            User userToUpdate = this.service.findById(id);

            user.setUserPrivate(userToUpdate.getUserPrivate());
            user.setRoles(userToUpdate.getRoles());
            user.setFolders(userToUpdate.getFolders());
            user.setToken(userToUpdate.getToken());
            user.setTokenExpiration(userToUpdate.getTokenExpiration());

            // SAVE & MAP
            User updated = this.service.save(user);
            userDTO = userMapper.entityToDto(updated);

            // ERROR
        } catch (NotFoundException ex){
            log.error("updateUser - " + ex.getMessage());
            res.put("sucess", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("updateUser - " + userDTO.toString());
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.ACCEPTED);
        res.put("data", userDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * delete user "/user/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Integer id
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        User user;

        try {
            // FIND & DELETE
            user = this.service.findById(id);
            this.service.delete(user);

            // ERROR
        } catch (NotFoundException ex){
            log.error("deleteUser - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("deleteUser - " + id);
        res.put("status", HttpStatus.OK);
        res.put("success", Boolean.TRUE);
        return ResponseEntity.ok(res);
    }



    /**
     * Validar token "/user/validateToken/{token}"
     * @param token
     * @return UserDTO
     */
    @PostMapping("/validateToken/{token}")
    public ResponseEntity<?> validateToken(
            @PathVariable String token
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserDTO newUserDTO;

        try {
            // FIND & MAP
            User userToValidate = this.service.findByToken(token);
            newUserDTO = this.userMapper.entityToDto(userToValidate);

            // ERROR
        } catch (SessionException ex){
            log.error("validateToken - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("sucess", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("validateToken - " + newUserDTO.toString());
        res.put("status", HttpStatus.OK);
        res.put("success", Boolean.TRUE);
        res.put("data", newUserDTO);
        return ResponseEntity.ok(res);
    }
}
