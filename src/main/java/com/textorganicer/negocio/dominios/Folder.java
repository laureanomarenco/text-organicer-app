package com.textorganicer.negocio.dominios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "folder")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "is_public")
    private Boolean is_public;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_user")
//    private User user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Page> pages;

}
