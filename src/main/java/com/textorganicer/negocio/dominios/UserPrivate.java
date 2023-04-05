package com.textorganicer.negocio.dominios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "user_private")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrivate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mail", length = 100, unique = true, nullable = false)
    @NotBlank(message = "El mail no puede estar vacío")
    @NotNull(message = "El mail no puede ser nulo")
    @Email(message = "El mail debe tener un formato de email válido")
    private String mail;

    @Column(name = "password", unique = true, nullable = false)
    @NotBlank(message = "El password no puede estar vacío")
    @NotNull(message = "El password no puede ser nulo")
    private String password;

    @Lob
    @Column(name = "salt")
    private byte[] salt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
