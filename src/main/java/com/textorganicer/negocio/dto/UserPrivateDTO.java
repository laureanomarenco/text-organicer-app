package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrivateDTO {
    private Integer id;
    private Integer user_id;
    @NotBlank(message = "El mail no puede estar vacío")
    @NotNull(message = "El mail no puede ser nulo")
    @Email(message = "El mail debe tener un formato de email válido")
    private String mail;

    @NotBlank(message = "El password no puede estar vacío")
    @NotNull(message = "El password no puede ser nulo")
    private String password;


}
