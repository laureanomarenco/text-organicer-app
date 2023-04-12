package com.textorganicer.controlador;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.servicios.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Controlador de User "/user"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;


    /**
     * getAll de Users "/user/"
     * @return List<UserDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    /**
     * getByID de User "user/{id}"
     * @param id
     * @return UserDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }


    /**
     * get User by username "user/username?username=xxxx"
     * @param username
     * @return UserDTO
     */
    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(
            @RequestParam String username
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByUsername(username));
    }


    /**
     * Post de User "/user"
     * @param user
     * @return UserPostDTO
     */
    @PostMapping
    public ResponseEntity<?> newUser(
            @Valid @RequestBody User user
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(user));
    }


    /**
     *
     * @param id
     * @param user
     * @return UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody User user
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(service.update(id, user));
    }


    /**
     * delete user "/user/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));
    }


    /**
     * Validar token "/user/validateToken/{token}"
     * @param token
     * @return UserDTO
     */
    @PostMapping("/validateToken/{token}")
    public ResponseEntity<?> validateToken(
            @PathVariable String token
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findByToken(token));
    }
}
