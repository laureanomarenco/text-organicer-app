package com.textorganicer.dominios;

public class Folder {
    private Integer id;
    private Integer idUser;
    private String nombre;
    private Boolean isPublic;

    public Folder() {
    }

    public Folder(Integer id, Integer idUser, String nombre, Boolean isPublic) {
        this.id = id;
        this.idUser = idUser;
        this.nombre = nombre;
        this.isPublic = isPublic;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", nombre='" + nombre + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
