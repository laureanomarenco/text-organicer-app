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
    private Integer user_id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @OneToOne
    @MapsId
    private User user;

}
