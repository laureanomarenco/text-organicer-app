package com.textorganicer.dominios;

public class User {
    private Integer id;
    private String username;
    private String imagen;

    public User() {
    }

    public User(Integer id, String username, String imagen) {
        this.id = id;
        this.username = username;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
