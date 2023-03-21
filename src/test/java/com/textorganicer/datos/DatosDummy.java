package com.textorganicer.datos;

import com.textorganicer.negocio.dominios.*;
import com.textorganicer.utils.RoleType;
import com.textorganicer.utils.SaltGenerator;
import com.textorganicer.utils.TokenGenerator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DatosDummy {

    public static User uno = getUserUno();
    public static UserPrivate unoPrivate = getUserPrivateUno();

    // ##########################
    // #######    USER    #######
    // ##########################
    public static User getUserUno() {
        User user = new User();
        user.setId(1);
        user.setUsername("Usuario");
        user.setImagen("http://");
        user.setToken(TokenGenerator.generateToken());
        user.setTokenExpiration(LocalDateTime.now().plusHours(10));
//        user.setUserPrivate(unoPrivate);
//        user.setFolders(folders);
//        user.setRoles(rolesUserUno);
        return user;
                //(0, "Usuario", "http://", TokenGenerator.generateToken(), LocalDateTime.now().plusHours(10), unoPrivate, folders, rolesUserUno);
    }

    public static UserPrivate getUserPrivateUno() {
        return new UserPrivate(0, "usuario@mail.com", "password", SaltGenerator.generateSalt(), uno);
    }

    public static User getUserDos() {
        User user = new User();
        user.setId(2);
        user.setUsername("Usuario2");
        user.setImagen("http://");
        user.setToken(TokenGenerator.generateToken());
        user.setTokenExpiration(LocalDateTime.now().plusHours(10));

        return user;
    }

    public static UserPrivate getUserPrivateDos() {
        return new UserPrivate(0, "usuario2@mail.com", "password2", SaltGenerator.generateSalt(), getUserUno());
    }


    // ############################
    // #######    FOLDER    #######
    // ############################
    public static Folder folderUno() {
        return new Folder(0, "FolderUno", "false", getUserUno(), pages, rolesFolderUno);
    }

    public static Folder folderDos() {
        return new Folder(0, "FolderDos", "false", getUserUno(), pages2, rolesFolderDos);
    }

    public static List<Folder> folders = Arrays.asList(
            folderUno(),
            folderDos()
    );

    public static List<Folder> folders2 = Arrays.asList();


    // ##########################
    // #######    PAGE    #######
    // ##########################
    public static Page pageUno() {
        return new Page(0, 1, "Titulo de pagina", "Subtitulo de la pagina", "Firma de la pagina", "Contenido de la página", folderUno());
    }

    public static Page pageDos() {
        return new Page(0, 2, "Titulo de pagina dos", "Subtitulo de la pagina dos", "Firma de la pagina dos", "Contenido de la página dos", folderUno());
    }

    public static Page pageTres() {
        return new Page(0, 3, "Titulo de pagina tres", "Subtitulo de la pagina tres", "Firma de la pagina tres", "Contenido de la página tres", folderUno());
    }

    public static Page pageCuatro() {
        return new Page(0, 1, "Titulo de pagina cuatro", "Subtitulo de la pagina cuatro", "Firma de la pagina cuatro", "Contenido de la página cuatro", folderDos());
    }

    public static Page pageCinco() {
        return new Page(0, 1, "Titulo de pagina cinco", "Subtitulo de la pagina cinco", "Firma de la pagina cinco", "Contenido de la página cinco", folderDos());
    }

    public static List<Page> pages = Arrays.asList(
            pageUno(),
            pageDos(),
            pageTres()
    );

    public static List<Page> pages2 = Arrays.asList(
            pageCuatro(),
            pageCinco()
    );

    // ##########################
    // #######    ROLE    #######
    // ##########################
    public static Role roleUno() {
        return new Role(0, getUserUno(), folderUno(), RoleType.OWNER);
    }

    public static Role roleDos() {
        return new Role(0, getUserUno(), folderDos(), RoleType.OWNER);
    }

    public static Role roleTres() {
        return new Role(0, getUserDos(), folderDos(), RoleType.COLLABORATOR);
    }

    public static List<Role> rolesUserUno = Arrays.asList(
            roleUno(),
            roleDos()
    );

    public static List<Role> rolesUserDos = Arrays.asList(
            roleTres()
    );

    public static List<Role> rolesFolderUno = Arrays.asList(
            roleUno()
    );

    public static List<Role> rolesFolderDos = Arrays.asList(
            roleDos(),
            roleTres()
    );
}
