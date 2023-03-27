package com.textorganicer.negocio.dominios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username", length = 16, unique = true, nullable = false)
    @NotBlank(message = "El username no puede estar vacío")
    @NotNull(message = "El username no puede ser nulo")
    @Size(min = 4, max = 16, message = "El username debe tener entre 4 y 16 caracteres")
    private String username;
    @Column(name = "imagen")
    private String imagen;
    private String token;
    @Column(name = "token_expiration")
    private LocalDateTime tokenExpiration;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private UserPrivate userPrivate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles;

}
