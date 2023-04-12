package com.textorganicer.controlador;

import com.textorganicer.excepciones.ErrorProcessException;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.servicios.UserPrivateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Controlador de UserPrivate "/user_private"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user_private")
@RequiredArgsConstructor
@Slf4j
public class UserPrivateController {
    private final UserPrivateService service;


    /**
     * getAll de UserPrivate "/user_private/"
     * @return List<UserPrivateDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllUsersPrivate() throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAll());
    }


    /**
     * Get UserPrivate by id "user_private/{id}"
     * @param id
     * @return UserPrivateDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserPrivateById(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }


    /**
     * Post userPrivate "/user_private/{user_id}
     * @param userPrivate
     * @param user_id
     * @return UserPrivateDTO
     */
    @PostMapping("/{user_id}")
    public ResponseEntity<?> newUserPrivate(
            @Valid @RequestBody UserPrivate userPrivate,
            @PathVariable Integer user_id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.save(user_id, userPrivate));
    }


    /**
     * "/login"
     * @param userPrivate
     * @return UserDTO
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserPrivate userPrivate
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.validate(userPrivate));
    }


    /**
     * updateUserPrivate "/user_private/{id}"
     * @param id
     * @param userPrivate
     * @return UserPrivateDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserPrivate(
            @PathVariable Integer id,
            @Valid @RequestBody UserPrivate userPrivate
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(userPrivate, id));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserEmail(
            @PathVariable Integer id,
            @Valid @RequestBody UserEmailUpdate userEmailUpdate
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateEmail(userEmailUpdate, id));
    }


    /**
     * deleteUserPrivate "/user_private/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserPrivate(
            @PathVariable Integer id
    ) throws ErrorProcessException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.delete(id));
    }
}