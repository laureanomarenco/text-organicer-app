package com.textorganicer.dominios;

public class Collaborator {
    private Integer id;
    private Integer idUser;
    private String username;
    private Integer idFolder;

    public Collaborator() {
    }
    public Collaborator(Integer id, Integer idUser, String username, Integer idFolder) {
        this.id = id;
        this.idUser = idUser;
        this.username = username;
        this.idFolder = idFolder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(Integer idFolder) {
        this.idFolder = idFolder;
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", username='" + username + '\'' +
                ", idFolder=" + idFolder +
                '}';
    }
}