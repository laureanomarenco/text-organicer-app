package com.textorganicer.controlador;

import com.textorganicer.excepciones.AuthException;
import com.textorganicer.excepciones.NotFoundException;
import com.textorganicer.negocio.dominios.User;
import com.textorganicer.negocio.dominios.UserPrivate;
import com.textorganicer.negocio.dto.UserDTO;
import com.textorganicer.negocio.dto.UserPrivateDTO;
import com.textorganicer.negocio.dto.mapper.UserMapper;
import com.textorganicer.negocio.dto.mapper.UserPrivateMapper;
import com.textorganicer.servicios.UserPrivateService;
import com.textorganicer.servicios.UserService;
import com.textorganicer.utils.HashGenerator;
import com.textorganicer.utils.SaltGenerator;
import com.textorganicer.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador de UserPrivate "/user_private"
 */
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user_private")
@Slf4j
public class UserPrivateController {

    private final UserPrivateMapper userPrivateMapper;
    private final UserMapper userMapper;
    private final UserPrivateService service;
    private final UserService userService;

    public UserPrivateController(UserPrivateMapper userPrivateMapper, UserMapper userMapper, UserPrivateService service, UserService userService) {
        this.userPrivateMapper = userPrivateMapper;
        this.userMapper = userMapper;
        this.service = service;
        this.userService = userService;
    }



    /**
     * getAll de UserPrivate "/user_private/"
     * @return List<UserPrivateDTO>
     */
    @GetMapping
    public ResponseEntity<?> getAllUsersPrivate() {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        List<UserPrivateDTO> allDTO;

        try {
            // GET & MAP
            List<UserPrivate> all = this.service.getAll();
            allDTO = all.stream()
                    .map(userPrivateMapper::entityToDto)
                    .collect(Collectors.toList());

            // ERROR
        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Get UserPrivate by id "user_private/{id}"
     * @param id
     * @return UserPrivateDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserPrivateById(
            @PathVariable Integer id
    ){
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserPrivateDTO userPrivateDTO;

        try {
            // FIND & MAP
            UserPrivate userPrivate = this.service.findById(id);
            userPrivateDTO = userPrivateMapper.entityToDto(userPrivate);

            // ERROR
        } catch (NotFoundException ex) {
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", userPrivateDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * Post userPrivate "/user_private/{user_id}
     * @param userPrivate
     * @param user_id
     * @return UserPrivateDTO
     */
    @PostMapping("/{user_id}")
    public ResponseEntity<?> newUserPrivate(
            @RequestBody UserPrivate userPrivate,
            @PathVariable Integer user_id
    ) {
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserPrivateDTO newUserPrivateDTO;

        try {
            // VALIDATE
            if(this.service.exists(userPrivate.getMail())) throw new RuntimeException("Ya existe un usuario con ese mail");

            // FIND & SET
            User user = this.userService.findById(user_id);

            byte[] salt = SaltGenerator.generateSalt();
            userPrivate.setSalt(salt);
            String hashedPassword = HashGenerator.hashPassword(userPrivate.getPassword(), salt);
            userPrivate.setPassword(hashedPassword);

            userPrivate.setUser(user);

            // SAVE & MAP
            UserPrivate newUserPrivate = this.service.save(userPrivate);
            newUserPrivateDTO = userPrivateMapper.entityToDto(newUserPrivate);

            // ERROR
        } catch (RuntimeException ex){
            log.error("postUserPrivate - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("postUserPrivate - " + newUserPrivateDTO.toString());
        res.put("status", HttpStatus.CREATED);
        res.put("success", Boolean.TRUE);
        res.put("data", newUserPrivateDTO);
        return ResponseEntity.ok(res);
    }



    /**
     * "/login"
     * @param userPrivate
     * @return UserDTO
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserPrivate userPrivate
    ) {
        Map<String, Object> res = new HashMap<>();

        UserDTO validatedUserDTO;

        try {
            UserPrivate userToValidate = this.service.findByMail(userPrivate.getMail());


            boolean isValid = this.service.validate(userPrivate, userToValidate);

            User validatedUser;
            if(!isValid) throw new AuthException("Password incorrecto");
            else validatedUser = userToValidate.getUser();



            String token = TokenGenerator.generateToken();
            validatedUser.setToken(token);

            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            validatedUser.setTokenExpiration(expirationDate);

            validatedUser = this.userService.save(validatedUser);

            validatedUserDTO = userMapper.entityToDto(validatedUser);

        } catch (NotFoundException ex){
            log.error("login - " + ex.getMessage());

            res.put("status", HttpStatus.UNAUTHORIZED);
            res.put("success", Boolean.FALSE);
            res.put("message", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        } catch (AuthException ex){
            log.error("login - " + ex.getMessage());

            res.put("status", HttpStatus.UNAUTHORIZED);
            res.put("success", Boolean.FALSE);
            res.put("message", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        log.info("login - " + validatedUserDTO.toString());

        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        res.put("data", validatedUserDTO);
        return ResponseEntity.ok(res);
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
            @RequestBody UserPrivate userPrivate
    ) {
        //CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserPrivateDTO updatedDTO;

        try {
            // FIND & SET
            UserPrivate userToUpdate = this.service.findById(id);
            userPrivate.setSalt(userToUpdate.getSalt());
            userPrivate.setPassword(HashGenerator.hashPassword(userPrivate.getPassword(), userToUpdate.getSalt()));
            userPrivate.setUser(userToUpdate.getUser());

            // SAVE & MAP
            UserPrivate updated = this.service.save(userPrivate);
            updatedDTO = userPrivateMapper.entityToDto(updated);

            // ERROR
        } catch (NotFoundException ex){
            log.error("updatePrivateUser - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("updatePrivateUser - " + updatedDTO.toString());
        res.put("status", HttpStatus.ACCEPTED);
        res.put("success", Boolean.TRUE);
        res.put("data", updatedDTO);
        return ResponseEntity.ok(res);
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
        // CONSTANT OBJECTS
        Map<String, Object> res = new HashMap<>();
        UserPrivate userPrivate;

        try {
            // FIND & DELETE
            userPrivate = this.service.findById(id);
            this.service.delete(userPrivate);

            // ERROR
        } catch (NotFoundException ex){
            log.error("deleteUserPrivate - " + ex.getMessage());
            res.put("status", HttpStatus.BAD_REQUEST);
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        // SUCCESS
        log.info("deleteUserPrivate - " + id);
        res.put("success", Boolean.TRUE);
        res.put("status", HttpStatus.OK);
        return ResponseEntity.ok(res);
    }

}