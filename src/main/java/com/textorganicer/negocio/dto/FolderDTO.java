package com.textorganicer.negocio.dto;

import com.textorganicer.negocio.dominios.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {
    private Integer id;
    private String nombre;
    private String is_public;
    private Integer id_user;
}
