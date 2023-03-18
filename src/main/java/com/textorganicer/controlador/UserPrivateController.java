package com.textorganicer.controlador;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/user_private")
public class UserPrivateController {

    private final UserPrivateMapper userPrivateMapper;
    private UserMapper userMapper;
    private final UserPrivateService service;
    private final UserService userService;

    public UserPrivateController(UserPrivateMapper userPrivateMapper, UserMapper userMapper, UserPrivateService service, UserService userService) {
        this.userPrivateMapper = userPrivateMapper;
        this.userMapper = userMapper;
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsersPrivate() {
        Map<String, Object> res = new HashMap<>();

        List<UserPrivateDTO> allDTO;

        try {
            List<UserPrivate> all = this.service.getAll();

            allDTO = all.stream()
                    .map(userPrivateMapper::entityToDto)
                    .collect(Collectors.toList());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", allDTO);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserPrivateById(@PathVariable Integer id){
        Map<String, Object> res = new HashMap<>();

        UserPrivateDTO userPrivateDTO;

        try {
            Optional<UserPrivate> userPrivate = this.service.findById(id);

            if(!userPrivate.isPresent()) throw new RuntimeException("No hay ningún usuario con ese id");

            userPrivateDTO = userPrivateMapper.entityToDto(userPrivate.get());

        } catch (RuntimeException ex) {
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", userPrivateDTO);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/{user_id}")
    public ResponseEntity<?> newUserPrivate(@RequestBody UserPrivate userPrivate,
                                            @PathVariable Integer user_id) {
        Map<String, Object> res = new HashMap<>();

        UserPrivateDTO newUserPrivateDTO;

        try {
            if(this.service.findByMail(userPrivate.getMail()).isPresent()) throw new RuntimeException("El username ya existe");

            Optional<User> user = this.userService.findById(user_id);

            if(!user.isPresent()) throw new RuntimeException("hubo un problema, no se creo bien el usuario público");

            byte[] salt = SaltGenerator.generateSalt();
            userPrivate.setSalt(salt);
            String hashedPassword = HashGenerator.hashPassword(userPrivate.getPassword(), salt);
            userPrivate.setPassword(hashedPassword);

            userPrivate.setUser(user.orElseThrow());
            UserPrivate newUserPrivate = this.service.save(userPrivate);
            newUserPrivateDTO = userPrivateMapper.entityToDto(newUserPrivate);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", newUserPrivateDTO);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserPrivate(@PathVariable Integer id,
                                               @RequestBody UserPrivate userPrivate) {

        Map<String, Object> res = new HashMap<>();

        UserPrivateDTO updatedDTO;
        try {
            Optional<UserPrivate> userToUpdate = this.service.findById(id);

            if(!userToUpdate.isPresent()) throw new RuntimeException("El usuario no existe");

            userPrivate.setPassword(HashGenerator.hashPassword(userPrivate.getPassword(), userToUpdate.get().getSalt()));

            userPrivate.setSalt(userToUpdate.get().getSalt());
            userPrivate.setUser(userToUpdate.get().getUser());
            UserPrivate updated = this.service.save(userPrivate);

            updatedDTO = userPrivateMapper.entityToDto(updated);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("mensaje", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", updatedDTO);

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

        UserDTO validatedUserDTO;

        try {
            Optional<UserPrivate> userToValidate = this.service.findByMail(userPrivate.getMail());
            if(!userToValidate.isPresent()) throw new RuntimeException("Mail incorrecto o usuario inexistente");

            boolean isValid = this.service.validate(userPrivate, userToValidate);

            User validatedUser;
            if(!isValid) throw new RuntimeException("Password incorrecto");
            else validatedUser = userToValidate.orElseThrow().getUser();



            String token = TokenGenerator.generateToken();
            validatedUser.setToken(token);

            LocalDateTime expirationDate = LocalDateTime.now().plusHours(10);
            validatedUser.setTokenExpiration(expirationDate);

            validatedUser = this.userService.save(validatedUser);

            validatedUserDTO = userMapper.entityToDto(validatedUser);

        } catch (RuntimeException ex){
            res.put("success", Boolean.FALSE);
            res.put("message", ex.getMessage());

            return ResponseEntity.badRequest()
                    .body(res);
        }

        res.put("success", Boolean.TRUE);
        res.put("data", validatedUserDTO);
        return ResponseEntity.ok(res);
    }

}
