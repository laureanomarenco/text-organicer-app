package com.textorganicer.negocio.dominios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//#TODO Reconvertir esta entidad para usar tabla de roles(puede ser tal vez una clase RoleTypes con constantes) para consumir desde otra entidad como tabla intermedia que funcionará para vincular user y folder. Está clase podría llamarse Role

@Entity
@Table(name = "Role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_folder")
    private Folder folder;

    private String RoleType;
}
