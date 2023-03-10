package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
    private Integer id;
    private String titulo;
    private String subtitulo;
    private String firma;
    private String contenido;
    private Integer id_folder;
}
