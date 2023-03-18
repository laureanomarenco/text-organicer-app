package com.textorganicer.negocio.dominios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    private String sal;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
