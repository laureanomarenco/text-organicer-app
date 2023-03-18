package com.textorganicer.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDTO {
    private Integer id;
    private String username;
    private String imagen;
    private String token;
}
