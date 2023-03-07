package com.textorganicer.dominios;

public class Page {
    private Integer id;
    private Integer idFolder;
    private String titulo;
    private String subtitulo;
    private String firma;
    private String contenido;

    public Page() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(Integer idFolder) {
        this.idFolder = idFolder;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", idFolder=" + idFolder +
                ", titulo='" + titulo + '\'' +
                ", subtitulo='" + subtitulo + '\'' +
                ", firma='" + firma + '\'' +
                ", contenido='" + contenido + '\'' +
                '}';
    }
}
