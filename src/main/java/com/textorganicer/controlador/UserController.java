package com.textorganicer.controlador;

import com.textorganicer.negocio.dominios.User;
import com.textorganicer.servicios.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@CrossOrigin("http://localhost:4200")
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    private List<User> users;

    public void UserController(Collection<User> users) {
        this.users = new ArrayList<>(Arrays
                .asList(
//                        new User(1, "Laureano", "url")
                ));
    }

    private final UserServiceImpl service;

    public UserController(UserServiceImpl service) {
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
    public User getUserById(@PathVariable String id){
        return this.users.stream()
                .filter(user -> user.getId().equals(id) )
                .findFirst()
                .orElse(new User());
    }

    @GetMapping("/username")
    public User getUserByUsername(@RequestParam String username) {
        return this.users.stream()
                .filter(user -> user.getUsername().equals(username) )
                .findFirst()
                .orElse(new User());
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

    private Optional<User> existsUser(String username) {
        return this.users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny();
    }
}
