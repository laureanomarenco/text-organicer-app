package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {
    private Integer id;
    private String nombre;
    private String is_public;
    private Integer id_user;
}
