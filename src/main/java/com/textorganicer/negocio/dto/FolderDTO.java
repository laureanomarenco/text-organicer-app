package com.textorganicer.negocio.dto;

import com.textorganicer.negocio.dominios.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDTO {
    private Integer id;
    private String nombre;
    private Boolean is_public;
    private Integer id_user;
}
