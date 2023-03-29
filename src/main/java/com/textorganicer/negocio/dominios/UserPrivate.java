package com.textorganicer.negocio.dominios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String mail;

    @Column(name = "password", unique = true, nullable = false)
    private String password;

    @Lob
    @Column(name = "salt")
    private byte[] salt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
