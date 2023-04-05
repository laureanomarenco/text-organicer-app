package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserEmailUpdate;
import com.textorganicer.negocio.dto.UserPrivateDTO;

import com.textorganicer.servicios.UserPrivateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


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
    public ResponseEntity<?> getAllUsersPrivate() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", this.service.getAll()));
    }


    /**
     * Get UserPrivate by id "user_private/{id}"
     * @param id
     * @return UserPrivateDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserPrivateById(
            @PathVariable Integer id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", this.service.findById(id)));
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
    ) {
        UserPrivateDTO newUserPrivateDTO = this.service.save(user_id, userPrivate);
        log.info("postUserPrivate - " + newUserPrivateDTO.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", newUserPrivateDTO));
    }


    /**
     * "/login"
     * @param userPrivate
     * @return UserDTO
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserPrivate userPrivate
    ) {
        UserDTO userValid = this.service.validate(userPrivate);
        log.info("login - " + userValid.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", userValid));
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
    ) {
        UserPrivateDTO updatedDTO = this.service.save(userPrivate, id);
        log.info("updatePrivateUser - " + updatedDTO.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", updatedDTO));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserEmail(
            @PathVariable Integer id,
            @Valid @RequestBody UserEmailUpdate userEmailUpdate
    ) {
        UserPrivateDTO updatedDTO = this.service.save(userEmailUpdate, id);
        log.info("updateUserEmail - " + updatedDTO.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "success", true,
                        "data", updatedDTO));
    }


    /**
     * deleteUserPrivate "/user_private/{id}"
     * @param id
     * @return ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserPrivate(
            @PathVariable Integer id
    ) {
        this.service.delete(id);
        log.info("deleteUserPrivate - " + id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("success", true));
    }

}