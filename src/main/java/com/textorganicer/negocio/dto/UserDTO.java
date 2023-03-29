package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    @NotBlank(message = "El username no puede estar vacío")
    @NotNull(message = "El username no puede ser nulo")
    @Size(min = 4, max = 16, message = "El username debe tener entre 4 y 16 caracteres")
    private String username;

    private String imagen;
    private String token;
    private List<FolderDTO> folders;
}
